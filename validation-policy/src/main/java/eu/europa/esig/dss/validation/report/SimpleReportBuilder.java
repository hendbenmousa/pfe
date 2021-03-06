/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jce.X509Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.TSLConstant;
import eu.europa.esig.dss.XmlDom;
import eu.europa.esig.dss.validation.policy.CertificateQualification;
import eu.europa.esig.dss.validation.policy.ProcessParameters;
import eu.europa.esig.dss.validation.policy.SignatureQualification;
import eu.europa.esig.dss.validation.policy.SignatureType;
import eu.europa.esig.dss.validation.policy.TLQualification;
import eu.europa.esig.dss.validation.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.policy.XmlNode;
import eu.europa.esig.dss.validation.policy.rules.AttributeName;
import eu.europa.esig.dss.validation.process.ValidationXPathQueryHolder;
import eu.europa.esig.dss.validation.process.dss.InvolvedServiceInfo;

import static eu.europa.esig.dss.validation.policy.rules.AttributeName.ID;
import static eu.europa.esig.dss.validation.policy.rules.AttributeName.TYPE;
import static eu.europa.esig.dss.validation.policy.rules.AttributeValue.COUNTERSIGNATURE;
import static eu.europa.esig.dss.validation.policy.rules.Indication.INDETERMINATE;
import static eu.europa.esig.dss.validation.policy.rules.Indication.INVALID;
import static eu.europa.esig.dss.validation.policy.rules.Indication.VALID;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.DETACHED_CONTENTS;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.DOCUMENT_NAME;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.ERROR;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.GLOBAL;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.INDICATION;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.INFO;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.NOT_AFTER;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.NOT_BEFORE;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.POLICY;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.POLICY_DESCRIPTION;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.POLICY_NAME;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.PRODUCTION_TIME;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIGNATURE;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIGNATURES_COUNT;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIGNATURE_FORMAT;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIGNATURE_LEVEL;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIGNED_BY;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIGNING_TIME;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SIMPLE_REPORT;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SUBJECT_DISTINGUISHED_NAME;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.SUB_INDICATION;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.TIMESTAMP;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.TIMESTAMPS;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.VALIDATION_TIME;
import static eu.europa.esig.dss.validation.policy.rules.NodeName.VALID_SIGNATURES_COUNT;
import static eu.europa.esig.dss.validation.policy.rules.SubIndication.SOME_NOT_VALID_SIGNATURES;
import static eu.europa.esig.dss.validation.policy.rules.SubIndication.UNEXPECTED_ERROR;

/**
 * This class builds a SimpleReport XmlDom from the diagnostic data and detailed validation report.
 */
public class SimpleReportBuilder implements ValidationXPathQueryHolder {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleReportBuilder.class);

	private final ValidationPolicy constraintData;
	private final DiagnosticData diagnosticData;

	private int totalSignatureCount = 0;
	private int validSignatureCount = 0;

	private Set<String> signatureIndicationSet = new HashSet<String>();
	private Set<String> signatureSubIndicationSet = new HashSet<String>();

	public SimpleReportBuilder(final ValidationPolicy constraintData, final DiagnosticData diagnosticData) {

		this.constraintData = constraintData;
		this.diagnosticData = diagnosticData;
	}

	/**
	 * @param signatureNode
	 * @param exception
	 */
	private static void notifyException(final XmlNode signatureNode, final Exception exception) {

		LOG.error(exception.getMessage(), exception);

		signatureNode.removeChild(INDICATION);
		signatureNode.removeChild(SUB_INDICATION);

		signatureNode.addChild(INDICATION, INDETERMINATE);
		signatureNode.addChild(SUB_INDICATION, UNEXPECTED_ERROR);

		final String message = DSSUtils.getSummaryMessage(exception, SimpleReportBuilder.class);
		signatureNode.addChild(INFO, message);
	}

	/**
	 * This method generates the validation simpleReport.
	 *
	 * @param params validation process parameters
	 * @return the object representing {@code SimpleReport}
	 */
	public SimpleReport build(final ProcessParameters params) {

		final XmlNode simpleReport = new XmlNode(SIMPLE_REPORT);
		simpleReport.setNameSpace(XmlDom.NAMESPACE);

		try {

			addPolicyNode(simpleReport);

			addValidationTime(params, simpleReport);

			addDocumentsName(simpleReport);

			addSignatures(params, simpleReport);

			addGlobalResult(params, simpleReport);
		} catch (Exception e) {

			if (!"WAS TREATED".equals(e.getMessage())) {

				notifyException(simpleReport, e);
			}
		}
		final Document reportDocument = simpleReport.toDocument(null);
		return new SimpleReport(reportDocument);
	}

	private void addPolicyNode(final XmlNode report) {

		final XmlNode policyNode = report.addChild(POLICY);
		final String policyName = constraintData.getPolicyName();
		final String policyDescription = constraintData.getPolicyDescription();
		policyNode.addChild(POLICY_NAME, policyName);
		if (!policyDescription.isEmpty()) {
		policyNode.addChild(POLICY_DESCRIPTION, policyDescription);
	}
	}

	private void addValidationTime(final ProcessParameters params, final XmlNode report) {

		final Date validationTime = params.getCurrentTime();
		report.addChild(VALIDATION_TIME, DSSUtils.formatDate(validationTime));
	}

	private void addDocumentsName(final XmlNode report) {

		final String documentName = diagnosticData.getValue("/DiagnosticData/DocumentName/text()");
		report.addChild(DOCUMENT_NAME, documentName);

		final List<XmlDom> detachedDocumentNameList = diagnosticData.getElements("/DiagnosticData/DetachedContents/DocumentName");
		if (detachedDocumentNameList.size() > 0) {

			final XmlNode detachedContentsXmlNode = report.addChild(DETACHED_CONTENTS, documentName);
			for (final XmlDom detachedDocumentName : detachedDocumentNameList) {

				detachedContentsXmlNode.addChild(DOCUMENT_NAME, detachedDocumentName.getText().trim());
	}
		}
	}

	private void addSignatures(final ProcessParameters params, final XmlNode reportXmlNode) throws DSSException {

		validSignatureCount = 0;
		totalSignatureCount = 0;

		final List<XmlDom> signatureXmlDomList = diagnosticData.getElements("/DiagnosticData/Signature");
		for (final XmlDom signatureXmlDom : signatureXmlDomList) {

			addSignature(params, reportXmlNode, signatureXmlDom);
		}
	}

	private void addGlobalResult(final ProcessParameters params, XmlNode reportXmlNode) {

		final XmlNode globalXmlNode = reportXmlNode.addChild(GLOBAL);
		final Conclusion generalStructureConclusion = params.getGeneralStructureConclusion();
		final String generalStructureIndication = generalStructureConclusion.getIndication();
		if (!VALID.equals(generalStructureIndication)) {

			globalXmlNode.addChild(INDICATION, generalStructureIndication);
			final String generalStructureSubIndication = generalStructureConclusion.getSubIndication();
			globalXmlNode.addChild(SUB_INDICATION, generalStructureSubIndication);

			final List<Conclusion.Error> errorList = generalStructureConclusion.getErrorList();
			for (final Conclusion.Error error : errorList) {

				final XmlNode xmlNode = globalXmlNode.addChild(ERROR, error.getValue());
				final HashMap<String, String> attributes = error.getAttributes();
				for (Map.Entry<String, String> attribute : attributes.entrySet()) {
					xmlNode.setAttribute(attribute.getKey(), attribute.getValue());
				}
			}
			// TODO-Bob (29/09/2015):  Warnings
		} else {

			if (signatureIndicationSet.size() == 1) {
				globalXmlNode.addChild(INDICATION, (String) signatureIndicationSet.toArray()[0]);
			} else {
				if (signatureIndicationSet.contains(INVALID)) {
					globalXmlNode.addChild(INDICATION, INVALID);
				} else {
					globalXmlNode.addChild(INDICATION, INDETERMINATE);
				}
			}
			if (signatureSubIndicationSet.size() == 1) {
				globalXmlNode.addChild(SUB_INDICATION, (String) signatureSubIndicationSet.toArray()[0]);
			} else if (signatureSubIndicationSet.size() > 1) {
				globalXmlNode.addChild(SUB_INDICATION, SOME_NOT_VALID_SIGNATURES);
			}
		}
		globalXmlNode.addChild(VALID_SIGNATURES_COUNT, Integer.toString(validSignatureCount));
		globalXmlNode.addChild(SIGNATURES_COUNT, Integer.toString(totalSignatureCount));
	}

	/**
	 * @param params              validation process parameters
	 * @param reportXmlNode
	 * @param signatureXmlDom the diagnosticSignature element in the diagnostic data
	 * @throws DSSException
	 */
	private void addSignature(final ProcessParameters params, final XmlNode reportXmlNode, final XmlDom signatureXmlDom) throws DSSException {

		totalSignatureCount++;

		final XmlNode signatureXmlNode = reportXmlNode.addChild(SIGNATURE);

		final String signatureId = signatureXmlDom.getAttribute(ID);
		signatureXmlNode.setAttribute(ID, signatureId);

		final String signatureType = signatureXmlDom.getAttribute(TYPE);
		addCounterSignature(signatureXmlDom, signatureXmlNode, signatureType);
		try {

			addSigningTime(signatureXmlDom, signatureXmlNode);
			addSignatureFormat(signatureXmlDom, signatureXmlNode);

			final String signCertificateId = signatureXmlDom.getValue("./SigningCertificate/@Id");
			final XmlDom signCertificateXmlDom = params.getCertificate(signCertificateId);

			addSignedBy(signatureXmlNode, signCertificateXmlDom);

			XmlDom bvData = params.getBvXmlDom();
			final XmlDom basicValidationConclusion = bvData.getElement("/BasicValidationData/Signature[@Id='%s']/Conclusion", signatureId);
			final XmlDom ltvDom = params.getLtvXmlDom();
			final XmlDom ltvConclusion = ltvDom.getElement("/LongTermValidationData/Signature[@Id='%s']/Conclusion", signatureId);
			final String ltvIndication = ltvConclusion.getValue(XP_INDICATION);
			final String ltvSubIndication = ltvConclusion.getValue(XP_SUB_INDICATION);
			final List<XmlDom> ltvInfoList = ltvConclusion.getElements(XP_INFO);
			final List<XmlDom> ltvWarningList = ltvConclusion.getElements(XP_WARNING);
			final List<XmlDom> ltvErrorList = ltvConclusion.getElements(XP_ERROR);

			String indication = ltvIndication;
			String subIndication = ltvSubIndication;
			List<XmlDom> infoList = new ArrayList<XmlDom>();
			infoList.addAll(ltvInfoList);

			final List<XmlDom> basicValidationInfoList = basicValidationConclusion.getElements(XP_INFO);
			final List<XmlDom> basicValidationWarningList = basicValidationConclusion.getElements(XP_WARNING);
			final List<XmlDom> basicValidationErrorList = basicValidationConclusion.getElements(XP_ERROR);

			// TODO-Bob (11/10/2015):
			final boolean requestedValidSignatureTimestamp = true; // <== from constraint

			if (!VALID.equals(ltvIndication) && !requestedValidSignatureTimestamp) {

				final String basicValidationConclusionIndication = basicValidationConclusion.getValue(XP_INDICATION);
				final String basicValidationConclusionSubIndication = basicValidationConclusion.getValue(XP_SUB_INDICATION);
				indication = basicValidationConclusionIndication;
				subIndication = basicValidationConclusionSubIndication;
				//				infoList = basicValidationInfoList;
				//				if (!VALID.equals(basicValidationConclusionIndication)) {
				//
				//					final XmlNode xmlNode = new XmlNode(NodeName.WARNING, LABEL_TINVTWS, null);
				//					final XmlDom xmlDom = xmlNode.toXmlDom();
				//					infoList.add(xmlDom);
				//				}
					}
			signatureIndicationSet.add(indication);
			signatureXmlNode.addChild(INDICATION, indication);
			if (VALID.equals(indication)) {
				validSignatureCount++;
			}
			if (!subIndication.isEmpty()) {

				signatureIndicationSet.add(subIndication);
				signatureXmlNode.addChild(SUB_INDICATION, subIndication);
			}
			final List<XmlDom> errorMessages = signatureXmlDom.getElements("./ErrorMessage");
				for (XmlDom errorDom : errorMessages) {

					String errorMessage = errorDom.getText();
					errorMessage = StringEscapeUtils.escapeXml(errorMessage);
				final XmlNode xmlNode = new XmlNode(INFO, errorMessage); // Internal exceptions are handled as Info
					final XmlDom xmlDom = xmlNode.toXmlDom();
					infoList.add(xmlDom);
				}
			//			if (!VALID.equals(ltvIndication)) {
			//
			//				addBasicInfo(signatureXmlNode, ltvErrorList);
			//				addBasicInfo(signatureXmlNode, basicValidationErrorList);
			//			}
			addBasicInfo(signatureXmlNode, ltvErrorList);
			addBasicInfo(signatureXmlNode, basicValidationWarningList);
			addBasicInfo(signatureXmlNode, infoList);

			addSignatureProfile(signatureXmlNode, signCertificateXmlDom);

			final XmlDom signatureScopes = signatureXmlDom.getElement("./SignatureScopes");
			addSignatureScope(signatureXmlNode, signatureScopes);

			final List<XmlDom> timestamps = signatureXmlDom.getElements("./Timestamps/Timestamp");
			addTimestamps(params, signatureXmlNode, timestamps);
		} catch (Exception e) {

			notifyException(signatureXmlNode, e);
			throw new DSSException("WAS TREATED", e);
		}
	}

	private void addTimestamps(ProcessParameters params, final XmlNode signatureNode, final List<XmlDom> timestamps) {

		if (timestamps.isEmpty()) {
			return;
		}
		final XmlNode timestampsXmlNode = signatureNode.addChild(TIMESTAMPS);
		for (final XmlDom timestamp : timestamps) {

			final String attributeValue = timestamp.getAttribute(AttributeName.TYPE);
			final XmlNode timestampXmlNode = timestampsXmlNode.addChild(TIMESTAMP).setAttribute(AttributeName.TYPE, attributeValue);
			final String productionTime = timestamp.getValue("./ProductionTime/text()");
			timestampXmlNode.addChild(PRODUCTION_TIME, productionTime);
			final String timestampSigningCertificateId = timestamp.getValue("./SigningCertificate/@Id");
			final XmlDom signCertificate = params.getCertificate(timestampSigningCertificateId);

			if (signCertificate != null) {

				final String dn = signCertificate.getValue("./SubjectDistinguishedName[@Format='RFC2253']/text()");
				final X500Principal x500Principal = new X500Principal(dn);
				final String sdnString = x500Principal.toString();
				final String escapedSdnString = StringEscapeUtils.escapeXml(sdnString);
				timestampXmlNode.addChild(SUBJECT_DISTINGUISHED_NAME, escapedSdnString);

				final String notBefore = signCertificate.getValue("./NotBefore/text()");
				final String notAfter = signCertificate.getValue("./NotAfter/text()");
				timestampXmlNode.addChild(NOT_BEFORE, notBefore);
				timestampXmlNode.addChild(NOT_AFTER, notAfter);
			}
		}
	}

	private void addCounterSignature(XmlDom diagnosticSignature, XmlNode signatureNode, String type) {

		if (COUNTERSIGNATURE.equals(type)) {

			signatureNode.setAttribute(AttributeName.TYPE, COUNTERSIGNATURE);
			final String parentId = diagnosticSignature.getValue("./ParentId/text()");
			signatureNode.setAttribute(AttributeName.PARENT_ID, parentId);
		}
	}

	private void addSignatureScope(final XmlNode signatureNode, final XmlDom signatureScopes) {
		if (signatureScopes != null) {
			signatureNode.addChild(signatureScopes);
		}
	}

	private void addBasicInfo(final XmlNode signatureNode, final List<XmlDom> basicValidationErrorList) {
		for (final XmlDom error : basicValidationErrorList) {

			signatureNode.addChild(error);
		}
	}

	private void addSigningTime(final XmlDom diagnosticSignature, final XmlNode signatureNode) {
		signatureNode.addChild(SIGNING_TIME, diagnosticSignature.getValue("./DateTime/text()"));
	}

	private void addSignatureFormat(final XmlDom diagnosticSignature, final XmlNode signatureNode) {
		signatureNode.setAttribute(SIGNATURE_FORMAT, diagnosticSignature.getValue("./SignatureFormat/text()"));
	}

	private void addSignedBy(final XmlNode signatureNode, final XmlDom signCert) {

		String signedBy = "?";
		String sdnString = "?";
		if (signCert != null) {

			final String dn = signCert.getValue("./SubjectDistinguishedName[@Format='RFC2253']/text()");
			final X500Principal x500Principal = new X500Principal(dn);
			sdnString = x500Principal.toString();
			final X509Principal x509Principal = new X509Principal(dn);
			final Vector<?> values = x509Principal.getValues(new ASN1ObjectIdentifier("2.5.4.3"));
			if (values != null && values.size() > 0) {

				final String string = (String) values.get(0);
				if (StringUtils.isNotBlank(string)) {
					signedBy = DSSUtils.replaceStrStr(string, "&", "&amp;");
				}
				if (StringUtils.isEmpty(signedBy)) {
					signedBy = DSSUtils.replaceStrStr(dn, "&", "&amp;");
				}
			}
		}
		signatureNode.addChild(SIGNED_BY, signedBy);
		final String escapedSdnString = StringEscapeUtils.escapeXml(sdnString);
		signatureNode.addChild(SUBJECT_DISTINGUISHED_NAME, escapedSdnString);

		final String notBefore = signCert.getValue("./NotBefore/text()");
		final String notAfter = signCert.getValue("./NotAfter/text()");
		signatureNode.addChild(NOT_BEFORE, notBefore);
		signatureNode.addChild(NOT_AFTER, notAfter);
	}

	private void addSignatureProfile(XmlNode signatureNode, XmlDom signCert) {
		/**
		 * Here we determine the type of the signature.
		 */
		SignatureType signatureType = SignatureType.NA;
		if (signCert != null) {

			signatureType = getSignatureType(signCert);
		}
		signatureNode.addChild(SIGNATURE_LEVEL, signatureType.name());
	}

	/**
	 * This method returns the type of the qualification of the signature (signing certificate).
	 *
	 * @param signCert
	 * @return
	 */
	private SignatureType getSignatureType(final XmlDom signCert) {

		final CertificateQualification certQualification = new CertificateQualification();
		certQualification.setQcp(signCert.getBoolValue("./QCStatement/QCP/text()"));
		certQualification.setQcpp(signCert.getBoolValue("./QCStatement/QCPPlus/text()"));
		certQualification.setQcc(signCert.getBoolValue("./QCStatement/QCC/text()"));
		certQualification.setQcsscd(signCert.getBoolValue("./QCStatement/QCSSCD/text()"));

		final TLQualification trustedListQualification = new TLQualification();

		final String caqc = InvolvedServiceInfo.getServiceTypeIdentifier(signCert);

		final List<String> qualifiers = InvolvedServiceInfo.getQualifiers(signCert);

		trustedListQualification.setCaqc(TSLConstant.CA_QC.equals(caqc));
		trustedListQualification.setQcCNoSSCD(InvolvedServiceInfo.isQC_NO_SSCD(qualifiers));
		trustedListQualification.setQcForLegalPerson(InvolvedServiceInfo.isQC_FOR_LEGAL_PERSON(qualifiers));
		trustedListQualification.setQcSSCDAsInCert(InvolvedServiceInfo.isQCSSCD_STATUS_AS_IN_CERT(qualifiers));
		trustedListQualification.setQcWithSSCD(qualifiers.contains(TSLConstant.QC_WITH_SSCD) || qualifiers.contains(TSLConstant.QC_WITH_SSCD_119612));

		final SignatureType signatureType = SignatureQualification.getSignatureType(certQualification, trustedListQualification);
		return signatureType;
	}
}
