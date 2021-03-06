/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p/>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation.process.subprocess;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.XmlDom;
import eu.europa.esig.dss.validation.policy.Constraint;
import eu.europa.esig.dss.validation.policy.ElementNumberConstraint;
import eu.europa.esig.dss.validation.policy.ProcessParameters;
import eu.europa.esig.dss.validation.policy.SignatureCryptographicConstraint;
import eu.europa.esig.dss.validation.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.policy.XmlNode;
import eu.europa.esig.dss.validation.policy.rules.ExceptionMessage;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.NodeName;
import eu.europa.esig.dss.validation.policy.rules.NodeValue;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.process.BasicValidationProcess;
import eu.europa.esig.dss.validation.report.Conclusion;

import static eu.europa.esig.dss.validation.policy.rules.MessageTag.ASCCM;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_1;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_1_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_2;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_2_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_3;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_3_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_4;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_4_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_5;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_5_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_6;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_6_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DCTIPER;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DCTIPER_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DNCSCVP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DNCSCVP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DNCTCVP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DNCTCVP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DNSTCVP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DNSTCVP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DSFCVP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_DSFCVP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_IACV;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_IACV_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ICERRM;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ICERRM_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ICRM;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ICRM_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPCHP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPCHP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPCIP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPCIP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPCTP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPCTP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPDOFP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPDOFP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPSLP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPSLP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPSTP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPSTP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPXTIP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISQPXTIP_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISSV;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_SAV_ISSV_ANS;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.EMPTY;
import static eu.europa.esig.dss.x509.TimestampType.ARCHIVE_TIMESTAMP;
import static eu.europa.esig.dss.x509.TimestampType.SIGNATURE_TIMESTAMP;
import static eu.europa.esig.dss.x509.TimestampType.VALIDATION_DATA_REFSONLY_TIMESTAMP;
import static eu.europa.esig.dss.x509.TimestampType.VALIDATION_DATA_TIMESTAMP;


/**
 * 5.5 Signature Acceptance Validation (SAV)
 * 5.5.1 Description
 * This building block covers any additional verification that shall be performed on the attributes/properties of the signature.
 * <p/>
 * 5.5.2 Inputs
 * Table 10: Inputs to the SVA process
 * - Input                                Requirement
 * - Signature                            Mandatory
 * - Cryptographic verification output    Optional
 * - Cryptographic Constraints            Optional
 * - Signature Constraints                Optional
 * <p/>
 * 5.5.3 Outputs
 * The process outputs one of the following indications:
 * Table 11: Outputs of the SVA process
 * - Indication: VALID
 * - Description: The signature is conformant with the validation constraints.
 * <p/>
 * - Indication: INVALID.SIG_CONSTRAINTS_FAILURE
 * - Description: The signature is not conformant with the validation constraints.
 * - Additional data items: The process shall output:
 * • The set of constraints that are not verified by the signature.
 * - Indication: INDETERMINATE.CRYPTO_CONSTRAINTS_FAILURE_NO_POE
 * - Description: At least one of the algorithms used in validation of the signature together with the size of the key, if applicable, used with that algorithm is no longer
 * considered reliable.
 * - Additional data items: The process shall output:
 * • A list of algorithms, together with the size of the key, if applicable, that have been used in validation of the signature but no longer are considered reliable together
 * with a time up to which each of the listed algorithms were considered secure.
 */
public class SignatureAcceptanceValidation extends BasicValidationProcess implements Indication, SubIndication, NodeName, NodeValue, ExceptionMessage {

	/**
	 * The following variables are used only in order to simplify the writing of the rules!
	 */

	protected ProcessParameters context;

	/**
	 * See {@link ProcessParameters#getCurrentValidationPolicy()}
	 */
	private ValidationPolicy validationPolicy;

	/**
	 * See {@link ProcessParameters#getCurrentTime()}
	 */
	private Date currentTime;

	/**
	 * See {@link ProcessParameters#getSignatureContext()}
	 */
	private XmlDom signatureContext;

	/**
	 * This node is used to add the constraint nodes.
	 */
	private XmlNode subProcessXmlNode;

	private void prepareParameters() {

		this.validationPolicy = context.getCurrentValidationPolicy();
		this.signatureContext = context.getSignatureContext();
		this.currentTime = context.getCurrentTime();

		isInitialised();
	}

	private void isInitialised() {

		assertValidationPolicy(validationPolicy, getClass());
		if (signatureContext == null) {
			throw new DSSException(String.format(EXCEPTION_TCOPPNTBI, getClass().getSimpleName(), "signatureContext"));
		}
		if (currentTime == null) {
			throw new DSSException(String.format(EXCEPTION_TCOPPNTBI, getClass().getSimpleName(), "currentTime"));
		}
	}

	/**
	 * 5.5.4 Processing
	 * This process consists in checking the Signature and Cryptographic Constraints against the signature. The
	 * general principle is as follows: perform the following for each constraint:
	 * <p/>
	 * • If the constraint necessitates processing a property/attribute in the signature, perform the processing of
	 * the property/attribute as specified from clause 5.5.4.1 to 5.5.4.8.
	 * <p/>
	 * 5.5.4.1 Processing AdES properties/attributes This clause describes the application of Signature Constraints on
	 * the content of the signature including the processing on signed and unsigned properties/attributes.
	 * Constraint XML description:
	 * <SigningCertificateChainConstraint><br>
	 * <MandatedSignedQProperties>
	 * <p/>
	 * Indicates the mandated signed qualifying properties that are mandated to be present in the signature.
	 * <p/>
	 * This method prepares the execution of the SAV process.
	 *
	 * @param context       validation process parameters
	 * @param parentXmlNode the parent process {@code XmlNode} to use to include the validation information
	 * @return the {@code Conclusion} which indicates the result of the process
	 */
	public Conclusion run(final ProcessParameters context, final XmlNode parentXmlNode) {

		if (parentXmlNode == null) {
			throw new DSSException(String.format(EXCEPTION_TCOPPNTBI, getClass().getSimpleName(), "parentXmlNode"));
		}
		this.context = context;
		prepareParameters();

		/**
		 * 5.5 Signature Acceptance Validation (SAV)
		 */
		subProcessXmlNode = parentXmlNode.addChild(SAV);

		final Conclusion conclusion = process();

		final XmlNode conclusionXmlNode = conclusion.toXmlNode();
		subProcessXmlNode.addChild(conclusionXmlNode);
		return conclusion;
	}

	/**
	 * This method implement SAV process.
	 *
	 * @return the {@code Conclusion} which indicates the result of the process
	 */
	private Conclusion process() {

		final Conclusion conclusion = new Conclusion();
		conclusion.setLocation(subProcessXmlNode.getLocation());


		// signature format: XAdES_BASELINE_B, XAdES_BASELINE_LTA, XAdES_*
		if (!checkSignatureFormatConstraint(conclusion)) {
			return conclusion;
		}

		// XSD structural validation (only for XAdES) to be moved at the beginning of the validation process: Draft ETSI EN 319 102-1 V1.0.0 (2015-07): 5.2.2 Format Checking
		if (!checkStructuralValidationConstraint(conclusion)) {
			return conclusion;
		}

		/**
		 * 5.5.4.1 Processing AdES properties/attributes
		 * This clause describes the application of Signature Constraints on the content of the signature including the processing
		 *on signed and unsigned properties/attributes.
		 */

		/**
		 * 5.5.4.2 Processing signing certificate reference constraint
		 * --> A part of this rule is already implemented within ISC.
		 * If the SigningCertificate property contains references to other certificates in the path, the verifier shall check
		 * each of the certificates in the certification path against these references as specified in steps 1 and 2 in clause 5.1.4.1
		 * (resp clause 5.1.4.2) for XAdES (resp CAdES).
		 * Should this property contain one or more references to certificates other than those present in the certification path, the
		 * verifier shall assume that a failure has occurred during the verification.
		 * Should one or more certificates in the certification path not be referenced by this property, the verifier shall assume that
		 * the verification is successful unless the signature policy mandates that references to all the certificates in the
		 * certification path "shall" be present.
		 *
		 * // TODO: (Bob: 2014 Mar 07) The check of the path against all elements (if exist) is not implemented (to be checked, maybe yes).
		 */

		// data-object-format
		if (!checkDataObjectFormatConstraint(conclusion)) {
			return conclusion;
		}

		// signing-time
		if (!checkSigningTimeConstraint(conclusion)) {
			return conclusion;
		}

		// content-type
		if (!checkContentTypeConstraint(conclusion)) {
			return conclusion;
		}

		// content-hints
		if (!checkContentHintsConstraint(conclusion)) {
			return conclusion;
		}

		// content-reference
		// TODO: (Bob: 2014 Mar 07)

		// content-identifier
		if (!checkContentIdentifierConstraint(conclusion)) {
			return conclusion;
		}

		// commitment-type-indication
		if (!checkCommitmentTypeIndicationConstraint(conclusion)) {
			return conclusion;
		}
		if (!checkCommitmentTypeIndicationObjectReferenceConstraint(conclusion)) {
			return conclusion;
		}

		// signer-location
		if (!checkSignerLocationConstraint(conclusion)) {
			return conclusion;
		}

		// content-time-stamp
		if (!checkContentTimeStampConstraints(conclusion)) {
			return conclusion;
		}

		if (!checkClaimedRoleConstraint(conclusion)) {
			return conclusion;
		}

		// <MandatedUnsignedQProperties>

		if (!checkCounterSignatureNumberConstraint(conclusion)) {
				return conclusion;
			}

		if (!checkSignatureTimestampNumberConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkArchiveTimestampNumberConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkCertifiedRoleConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkCompleteCertificateRefsConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkCompleteRevocationRefsConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkRefsOnlyTimestampNumberConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkCertificateValuesConstraint(conclusion)) {
			return conclusion;
		}

		if (!checkRevocationValuesConstraint(conclusion)) {
			return conclusion;
		}
		// Main signature cryptographic constraints validation
		if (!checkMainSignatureCryptographicConstraint(conclusion)) {
			return conclusion;
		}

		// This validation process returns VALID
		conclusion.setIndication(VALID);
		return conclusion;
	}

	private boolean checkRefsOnlyTimestampNumberConstraint(final Conclusion conclusion) {

		final ElementNumberConstraint constraint = validationPolicy.getValidationDataTimestampNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_1);
		final List<XmlDom> refsOnlyTimestampXmlDom = signatureContext
			  .getElements("./Timestamps/Timestamp[@Type='%s' or @Type='%s']", VALIDATION_DATA_REFSONLY_TIMESTAMP, VALIDATION_DATA_TIMESTAMP);
		constraint.setIntValue(refsOnlyTimestampXmlDom.size());
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_1_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	private boolean checkCompleteCertificateRefsConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getCompleteCertificateRefsConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_3);
		final boolean exist = signatureContext.getBoolValue("./CompleteCertificateRefs/text()");
		constraint.setValue(exist);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_3_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	private boolean checkCompleteRevocationRefsConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getCompleteRevocationRefsConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_4);
		final boolean exist = signatureContext.getBoolValue("./CompleteRevocationRefs/text()");
		constraint.setValue(exist);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_4_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	private boolean checkCertificateValuesConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getCertificateValuesConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_5);
		final boolean exist = signatureContext.getBoolValue("./CertificateValues/text()");
		constraint.setValue(exist);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_5_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	private boolean checkRevocationValuesConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getRevocationValuesConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_6);
		final boolean exist = signatureContext.getBoolValue("./RevocationValues/text()");
		constraint.setValue(exist);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_6_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of signature format: XAdES_BASELINE_LTA...
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkSignatureFormatConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getSignatureFormatConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_DSFCVP);
		final String signatureFormat = signatureContext.getValue("./SignatureFormat/text()");
		constraint.setValue(signatureFormat);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_DSFCVP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.checkInList();
	}

	/**
	 * Check of structural validation (only for XAdES signature: XSD schema validation)
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkStructuralValidationConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getStructuralValidationConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISSV);
		final boolean structureValid = signatureContext.getBoolValue("./StructuralValidation/Valid/text()");
		constraint.setValue(structureValid);
		final String message = signatureContext.getValue("./StructuralValidation/Message/text()");
		if (StringUtils.isNotBlank(message)) {
			constraint.setAttribute("Log", message);
		}
		constraint.setIndications(INVALID, FORMAT_FAILURE, BBB_SAV_ISSV_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of data-object-format
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkDataObjectFormatConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getDataObjectFormatConstraint();
		if (constraint == null) {
			return true;
		}
		final List<XmlDom> referenceXmlDomList = signatureContext.getElements("./BasicSignature/References/Reference");
		if (referenceXmlDomList.isEmpty()) { // Test only if XAdES
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPDOFP);
		boolean dataObjectFormatOk = referenceXmlDomList.size() > 0 ? true : false;
		for (final XmlDom referenceXmlDom : referenceXmlDomList) {

			final XmlDom dataObjectFormatElement = referenceXmlDom.getElement("./DataObjectFormat");
			if (dataObjectFormatElement != null) {

				final boolean dataObjectFormat = dataObjectFormatElement.getBoolValue("./text()");
				if (!dataObjectFormat) {

					dataObjectFormatOk = false;
					final String uri = referenceXmlDom.getValue("./Uri/text()");
					constraint.setAttribute(URI, uri); // TODO-Bob (13/10/2015):  It would be better to point to the Id of the reference
					break;
				}
			}
		}
		constraint.setValue(dataObjectFormatOk);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPDOFP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of signing-time
	 * <p/>
	 * 5.5.4.3 Processing claimed signing time
	 * If the signature constraints contain constraints regarding this property, the verifying application shall follow its rules for
	 * checking this signed property.
	 * Otherwise, the verifying application shall make the value of this property/attribute available to its DA, so that it may
	 * decide additional suitable processing, which is out of the scope of the present document.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkSigningTimeConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getSigningTimeConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPSTP);
		final String signingTime = signatureContext.getValue("./DateTime/text()");
		constraint.setValue(StringUtils.isNotBlank(signingTime));
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPSTP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of content-type (signed property)
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkContentTypeConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getContentTypeConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPCTP);
		final String contentType = signatureContext.getValue("./ContentType/text()");
		constraint.setValue(contentType);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPCTP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of content-hints
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkContentHintsConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getContentHintsConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPCHP);
		final String contentHints = signatureContext.getValue("./ContentHints/text()");
		constraint.setValue(contentHints);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPCHP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of content-identifier
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkContentIdentifierConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getContentIdentifierConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPCIP);
		final String contentIdentifier = signatureContext.getValue("./ContentIdentifier/text()");
		constraint.setValue(contentIdentifier);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPCIP_ANS);
		//constraint.setAttribute()
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of commitment-type-indication
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkCommitmentTypeIndicationConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getCommitmentTypeIndicationConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPXTIP);
		// TODO: A set of commitments must be checked
		final String commitmentTypeIndicationIdentifier = signatureContext.getValue("./CommitmentTypeIndication[1]/Identifier/text()");
		constraint.setValue(commitmentTypeIndicationIdentifier);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPXTIP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.checkInList();
	}

	/**
	 * Check of commitment-type-indication
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkCommitmentTypeIndicationObjectReferenceConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getCommitmentTypeIndicationConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_DCTIPER);
		// TODO: A set of commitments must be checked
		final List<XmlDom> objectReferenceXmlDomList = signatureContext.getElements("./CommitmentTypeIndication[1]/ObjectReference");
		constraint.setValue(objectReferenceXmlDomList.size() > 0 ? TRUE : FALSE);
		for (final XmlDom objectReferenceXmlDom : objectReferenceXmlDomList) {

			final String objectReferenceValue = objectReferenceXmlDom.getText();
			final String objectReferenceExists = objectReferenceXmlDom.getAttribute("Exists");
			if (FALSE.equals(objectReferenceExists)) {
				constraint.setValue(FALSE);
				constraint.setAttribute(OBJECT_REFERENCE, objectReferenceValue);
			}
		}
		constraint.setExpectedValue(TRUE);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_DCTIPER_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of signer-location
	 * <p/>
	 * 5.5.4.5 Processing indication of production place of the signature
	 * If the signature constraints contain constraints regarding this property, the verifying application shall follow its rules for
	 * checking this signed property.
	 * Otherwise, the verifying application shall make the value of this property/attribute available to its DA, so that it may
	 * decide additional suitable processing, which is out of the scope of the present document.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkSignerLocationConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getSignerLocationConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ISQPSLP);
		String signatureProductionPlace = signatureContext.getValue("./SignatureProductionPlace/text()");
		final XmlDom signProductionPlaceXmlDom = signatureContext.getElement("./SignatureProductionPlace");
		if (signProductionPlaceXmlDom != null) {

			final List<XmlDom> elements = signProductionPlaceXmlDom.getElements("./*");
			for (final XmlDom element : elements) {

				if (!signatureProductionPlace.isEmpty()) {

					signatureProductionPlace += "; ";
				}
				signatureProductionPlace += element.getName() + ": " + element.getText();
			}
		}
		constraint.setValue(signatureProductionPlace);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ISQPSLP_ANS);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of content-time-stamp: verifies whether a content-timestamp (or similar) element is present
	 * <p/>
	 * 5.5.4.6 Processing Time-stamps on signed data objects<br>
	 * If the signature constraints contain specific constraints for content-time-stamp attributes, the SVA shall
	 * check that they are satisfied. To do so, the SVA shall do the following steps for each content-time-stamp
	 * attribute:<br>
	 * 1) Perform the Validation Process for AdES Time-Stamps as defined in clause 7 with the time-stamp token of the
	 * content-time-stamp attribute.<br>
	 * 2) Check the message imprint: check that the hash of the signed data obtained using the algorithm indicated in
	 * the time-stamp token matches the message imprint indicated in the token.<br>
	 * 3) Apply the constraints for content-time-stamp attributes to the results returned in the previous steps. If
	 * any check fails, return INVALID/SIG_CONSTRAINTS_FAILURE with an explanation of the unverified constraint.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkContentTimeStampConstraints(final Conclusion conclusion) {

		final List<String> contentTimestampTypeList = validationPolicy.getContentTimestampTypeList();
		final List<String> contentTimestampIdList = getContentTimestampIdList(contentTimestampTypeList, signatureContext);
		context.setContentTimestampIdList(contentTimestampIdList);
		final ElementNumberConstraint constraint = validationPolicy.getContentTimestampNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_DNCTCVP);
		constraint.setIntValue(contentTimestampIdList.size());
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_DNCTCVP_ANS);
		constraint.setAttribute("ExpectedTypeList", contentTimestampTypeList.toString());
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

	/**
	 * Check of unsigned qualifying property: claimed roles
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkClaimedRoleConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getClaimedRoleConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_ICRM);
		final List<XmlDom> claimedRolesXmlDom = signatureContext.getElements("./ClaimedRoles/ClaimedRole");
		final List<String> claimedRoles = XmlDom.convertToStringList(claimedRolesXmlDom);
		// TODO (Bob) to be implemented for each claimed role. Attendance must be taken into account.
		final String attendance = validationPolicy.getClaimedRolesAttendance();
		String claimedRole = null;
		for (String claimedRole_ : claimedRoles) {

			claimedRole = claimedRole_;
			break;
		}
		if ("ANY".equals(attendance)) {
			constraint.setExpectedValue("*");
		}
		constraint.setValue(claimedRole);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ICRM_ANS);
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.checkInList();
		return check;
	}

	/**
	 * Check of unsigned qualifying property: CounterSignature.
	 * The number of detected SignatureTimestamps is check against the validation policy. Even not valid timestamps are taken into account.
	 * <p/>
	 * 5.5.4.7 Processing Countersignatures
	 * If the signature constraints define specific constraints for countersignature attributes, the SVA shall check that they are
	 * satisfied. To do so, the SVA shall do the following steps for each countersignature attribute:
	 * 1) Perform the validation process for AdES-BES/EPES using the countersignature in the property/attribute and
	 * the signature value octet string of the signature as the signed data object.
	 * 2) Apply the constraints for countersignature attributes to the result returned in the previous step. If any check
	 * fails, return INVALID/SIG_CONSTRAINTS_FAILURE with an explanation of the unverified constraint.
	 * If the signature constraints do not contain any constraint on countersignatures, the SVA may still verify the
	 * countersignature and provide the results in the validation report. However, it shall not consider the signature validation
	 * to having failed if the countersignature could not be verified.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkCounterSignatureNumberConstraint(final Conclusion conclusion) {

		ElementNumberConstraint constraint = validationPolicy.getCounterSignatureNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_DNCSCVP);
		final XmlDom diagnosticDataXmlDom = context.getDiagnosticData();
		final long counterSignatureCount = diagnosticDataXmlDom.getCountValue("count(./Signature[@ParentId='%s'])", context.getSignatureId());
		constraint.setIntValue((int) counterSignatureCount);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_DNCSCVP_ANS);
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.check();
		return check;
	}


	/**
	 * Check of unsigned qualifying property: SignatureTimestamp.
	 * The number of detected SignatureTimestamps is check against the validation policy. Even not valid timestamps are taken into account.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkSignatureTimestampNumberConstraint(final Conclusion conclusion) {

		final ElementNumberConstraint constraint = validationPolicy.getSignatureTimestampNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_DNSTCVP);
		final List<XmlDom> signatureTimestampXmlDom = signatureContext.getElements(XP_TIMESTAMPS, SIGNATURE_TIMESTAMP);
		constraint.setIntValue(signatureTimestampXmlDom.size());
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_DNSTCVP_ANS);
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.check();
		return check;
	}

	/**
	 * Check of unsigned qualifying property: ArchiveTimestamp.
	 * The number of detected SignatureTimestamps is check against the validation policy. Even not valid timestamps are taken into account.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkArchiveTimestampNumberConstraint(final Conclusion conclusion) {

		final ElementNumberConstraint constraint = validationPolicy.getArchiveTimestampNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, BBB_SAV_2);
		final List<XmlDom> archiveTimestampXmlDom = signatureContext.getElements(XP_TIMESTAMPS, ARCHIVE_TIMESTAMP);
		constraint.setIntValue(archiveTimestampXmlDom.size());
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_2_ANS);
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.check();
		return check;
	}

	/**
	 * Check of: main signature certified role.
	 * <p/>
	 * 5.5.4.8 Processing signer attributes/roles
	 * If the signature constraints define specific constraints for certified attributes/roles, the SVA shall perform the following
	 * checks:
	 * 1) The SVA shall verify the validity of the attribute certificate(s) present in this property/attribute following the
	 * rules established in [6].
	 * 2) The SVA shall check that the attribute certificate(s) actually match the rules specified in the input constraints.
	 * If the signature rules do not specify rules for certified attributes/roles, the SVA shall make the value of this
	 * property/attribute available to its DA so that it may decide additional suitable processing, which is out of the scope of
	 * the present document.
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkCertifiedRoleConstraint(final Conclusion conclusion) {

		final Constraint constraint = validationPolicy.getCertifiedRoleConstraint();
		if (constraint == null) {
			return true;
		}
		final XmlNode xmlNode = constraint.create(subProcessXmlNode, BBB_SAV_IACV);

		final XmlDom certifiedRolesXmlDom = signatureContext.getElement("./CertifiedRoles");
		Date notBefore = null;
		Date notAfter = null;

		boolean valid = false;
		if (certifiedRolesXmlDom != null) {

			notBefore = certifiedRolesXmlDom.getTimeValue(XP_NOT_BEFORE);
			notAfter = certifiedRolesXmlDom.getTimeValue(XP_NOT_AFTER);
			valid = currentTime.after(notBefore) && currentTime.before(notAfter);
		}
		constraint.setExpectedValue(TRUE);
		constraint.setValue(valid);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_IACV_ANS);
		constraint.setAttribute(CURRENT_TIME, DSSUtils.formatDate(currentTime)).setAttribute(NOT_BEFORE, DSSUtils.formatDate(notBefore))
			  .setAttribute(NOT_AFTER, DSSUtils.formatDate(notAfter));
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.check();
		if (!check) {
			return false;
		}
		xmlNode.addChild(INFO, "WARNING: The attribute certificate is not cryptographically validated.");

		constraint.clearAttributes();

		constraint.create(subProcessXmlNode, BBB_SAV_ICERRM);

		final List<XmlDom> certifiedRoleXmlDomList = certifiedRolesXmlDom.getElements("./CertifiedRole");
		final List<String> certifiedRoles = XmlDom.convertToStringList(certifiedRoleXmlDomList);
		constraint.setExpectedValue(constraint.getIdentifierList().toString());
		constraint.setValue(certifiedRoles);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_SAV_ICERRM_ANS);

		return constraint.checkInList();
	}

	/**
	 * Check of: main signature cryptographic verification
	 *
	 * @param conclusion the conclusion to use to add the result of the check.
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkMainSignatureCryptographicConstraint(final Conclusion conclusion) {

		final SignatureCryptographicConstraint constraint = validationPolicy.getSignatureCryptographicConstraint(MAIN_SIGNATURE);
		if (constraint == null) {
			return true;
		}
		constraint.create(subProcessXmlNode, ASCCM);
		constraint.setCurrentTime(currentTime);
		constraint.setEncryptionAlgorithm(signatureContext.getValue(XP_ENCRYPTION_ALGO_USED_TO_SIGN_THIS_TOKEN));
		constraint.setDigestAlgorithm(signatureContext.getValue(XP_DIGEST_ALGO_USED_TO_SIGN_THIS_TOKEN));
		constraint.setKeyLength(signatureContext.getValue(XP_KEY_LENGTH_USED_TO_SIGN_THIS_TOKEN));
		constraint.setIndications(INDETERMINATE, CRYPTO_CONSTRAINTS_FAILURE_NO_POE, EMPTY);
		constraint.setConclusionReceiver(conclusion);

		return constraint.check();
	}

}
