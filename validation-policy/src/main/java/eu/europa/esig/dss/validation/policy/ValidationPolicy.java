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
package eu.europa.esig.dss.validation.policy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import eu.europa.esig.dss.XmlDom;
import eu.europa.esig.dss.validation.policy.rules.AttributeName;
import eu.europa.esig.dss.validation.policy.rules.RuleConstant;

/**
 * This class encapsulates the constraint file that controls the policy to be used during the validation process. This is the base class used to implement a specific validation
 * policy
 */
public abstract class ValidationPolicy extends XmlDom implements RuleConstant, AttributeName {

	protected Map<String, Constraint> customConstraintMap;

	public ValidationPolicy(final Document document) {

		super(document);
	}

	/**
	 * @param customConstraintMap
	 */
	public void setCustomConstraintMap(final Map<String, Constraint> customConstraintMap) {
		this.customConstraintMap = customConstraintMap;
	}

	/**
	 * @return
	 */
	public abstract boolean isRevocationFreshnessToBeChecked();

	/**
	 * @return
	 */
	public abstract String getFormatedMaxRevocationFreshness();

	/**
	 * This function returns the maximum duration in milliseconds for which the revocation data are considered fresh.
	 *
	 * @return
	 */
	public abstract Long getMaxRevocationFreshness();

	/**
	 * This function returns the algorithm expiration date extracted from the 'constraint.xml' file. If the TAG AlgoExpirationDate is not present within the
	 * constraints {@code null} is returned.
	 *
	 * @param algorithm algorithm (SHA1, SHA256, RSA2048...) to be checked
	 * @return expiration date or null
	 */
	public abstract Date getAlgorithmExpirationDate(String algorithm);

	/**
	 * Indicates if the signature policy should be checked.
	 *
	 * @return {@code SignaturePolicyConstraint} If AcceptablePolicies element is absent within the constraint file then null is returned, otherwise the list of identifiers is
	 * initialised.
	 */
	public abstract SignaturePolicyConstraint getSignaturePolicyConstraint();

	/**
	 * Indicates if the signature format (XAdES_BASELINE_B, XAdES_BASELINE_LT...) should be checked.
	 * // TODO-Bob (08/10/2015):  This constraint can be converted in two distinct:
	 * // TODO-Bob (08/10/2015):  - format: CMS/XMLDsig, PDF, CAdES, XAdES, PAdES & ASiC
	 * // TODO-Bob (08/10/2015):  - level: -B, -T, -LT...
	 *
	 * @return {@code Constraint} If AcceptableSignatureFormats element is absent within the constraint file then null is returned, otherwise the list of acceptable signature
	 * format is initialised.
	 */
	public abstract Constraint getSignatureFormatConstraint();

	/**
	 * Indicates if the structural validation should be checked. If StructuralValidation element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if StructuralValidation element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getStructuralValidationConstraint();

	/**
	 * Indicates if the signed property: data-object-format should be checked.
	 *
	 * @return {@code Constraint} if DataObjectFormat element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getDataObjectFormatConstraint();

	/**
	 * Indicates if the signed property: signing-time should be checked. If SigningTime element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if SigningTime element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningTimeConstraint();

	/**
	 * Indicates if the signed property: content-type should be checked. If ContentType element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if ContentType element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getContentTypeConstraint();

	/**
	 * Indicates if the signed property: content-hints should be checked. If ContentHints element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if ContentHints element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getContentHintsConstraint();

	/**
	 * Indicates if the signed property: content-identifier should be checked. If ContentIdentifier element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if ContentIdentifier element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getContentIdentifierConstraint();

	/**
	 * Indicates if the signed property: commitment-type-indication should be checked. If CommitmentTypeIndication element is absent within the constraint file then null is
	 * returned, otherwise the list of identifiers is initialised.
	 *
	 * @return {@code Constraint} if CommitmentTypeIndication element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getCommitmentTypeIndicationConstraint();

	/**
	 * Indicates if the signed property: signer-location should be checked. If SignerLocation element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if SignerLocation element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSignerLocationConstraint();

	/**
	 * Indicates if the signed property: content-time-stamp should be checked. If ContentTimeStamp element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if ContentTimeStamp element is present in the constraint file, null otherwise.
	 */
	public abstract ElementNumberConstraint getContentTimestampNumberConstraint();

	/**
	 * @return the {@code List} of content timestamp types to be taken into account, or an empty {@code List} if each content-timestamp should be considered
	 */
	public abstract List<String> getContentTimestampTypeList();

	/**
	 * Indicates if the signed property: content-time-stamp should be checked. If ClaimedRoles element is absent within the constraint file then null is returned.
	 *
	 * @return {@code Constraint} if ClaimedRoles element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getClaimedRoleConstraint();

	/**
	 * Return the mandated signer role.
	 *
	 * @return
	 */
	public abstract List<String> getClaimedRoles();

	/**
	 * The {@code Constraint} is initialised with the values extracted from the constraint file.
	 *
	 * @return {@code Constraint} if CertifiedRoles element is present in the constraint file, null otherwise
	 */
	public abstract Constraint getCertifiedRoleConstraint();

	/**
	 * Returns the name of the policy.
	 *
	 * @return
	 */
	public abstract String getPolicyName();

	/**
	 * Returns the policy description.
	 *
	 * @return
	 */
	public abstract String getPolicyDescription();

	/**
	 * Returns the timestamp delay in milliseconds.
	 *
	 * @return
	 */
	public abstract Long getTimestampDelayTime();

	public abstract String getClaimedRolesAttendance();

	/**
	 * This method creates the {@code SignatureCryptographicConstraint} corresponding to the context parameter. If AcceptableEncryptionAlgo is not present in the constraint file
	 * the null is returned.
	 *
	 * @param context The context of the signature cryptographic constraints: MainSignature, Timestamp, Revocation
	 * @return {@code SignatureCryptographicConstraint} if AcceptableEncryptionAlgo for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract SignatureCryptographicConstraint getSignatureCryptographicConstraint(String context);

	/**
	 * This method creates the {@code SignatureCryptographicConstraint} corresponding to the context parameter. If AcceptableEncryptionAlgo is not present in the constraint file
	 * the null is returned.
	 *
	 * @param context    The context of the signature cryptographic constraints: MainSignature, Timestamp, Revocation
	 * @param subContext the sub context of the signature cryptographic constraints: EMPTY (signature itself), SigningCertificate, CACertificate
	 * @return {@code SignatureCryptographicConstraint} if AcceptableEncryptionAlgo for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract SignatureCryptographicConstraint getSignatureCryptographicConstraint(String context, String subContext);

	/**
	 * This method creates the {@code SignatureCryptographicConstraint} corresponding to the context parameter. If AcceptableEncryptionAlgo is not present in the constraint file
	 * the null is returned.
	 *
	 * @param rootXPathQuery The context of the signature cryptographic constraints is included within the XPath query.
	 * @param context        The context of the signature cryptographic constraints: MainSignature, Timestamp, Revocation
	 * @param subContext     the sub context of the signature cryptographic constraints: EMPTY (signature itself), SigningCertificate, CACertificate
	 * @return {@code SignatureCryptographicConstraint} if AcceptableEncryptionAlgo for a given context element is present in the constraint file, null otherwise.
	 */
	protected abstract SignatureCryptographicConstraint getSignatureCryptographicConstraint_(String rootXPathQuery, String context, String subContext);

	/**
	 * @return
	 */
	public abstract ManifestCryptographicConstraint getManifestCryptographicConstraint();

	/**
	 * @param context
	 * @return {@code Constraint} if key-usage for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateKeyUsageConstraint(String context);

	/**
	 * @param context
	 * @param subContext
	 * @return {@code Constraint} if Expiration for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract CertificateExpirationConstraint getSigningCertificateExpirationConstraint(String context, String subContext);

	/**
	 * This constraint requests the presence of the trust anchor in the certificate chain.
	 *
	 * @param context
	 * @return {@code Constraint} if ProspectiveCertificateChain element for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getProspectiveCertificateChainConstraint(String context);

	/**
	 * @param context
	 * @param subContext
	 * @return {@code Constraint} if Signature for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getCertificateSignatureConstraint(String context, String subContext);

	/**
	 * @param context
	 * @return {@code Constraint} if RevocationDataAvailable for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getRevocationDataAvailableConstraint(String context, String subContext);

	/**
	 * @param context
	 * @return {@code Constraint} if RevocationDataIsTrusted for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getRevocationDataIsTrustedConstraint(String context, String subContext);

	/**
	 * @param context
	 * @return {@code Constraint} if RevocationDataFreshness for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getRevocationDataFreshnessConstraint(String context, String subContext);

	/**
	 * @return {@code Constraint} if Revoked for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateRevokedConstraint(String context, String subContext);

	/**
	 * @return {@code Constraint} if OnHold for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateOnHoldConstraint(String context, String subContext);

	/**
	 * @return {@code Constraint} if the TSLValidity for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateTSLValidityConstraint(String context);

	/**
	 * @return {@code Constraint} if TSLStatus for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateTSLStatusConstraint(String context);

	/**
	 * @return {@code Constraint} if the TSLValidity for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateTSLStatusAndValidityConstraint(String context);

	/**
	 * @param context of the certificate: main signature, timestamp, revocation data
	 * @return {@code Constraint} if Revoked for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getIntermediateCertificateRevokedConstraint(String context);

	/**
	 * @return {@code Constraint} if CertificateChain for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getChainConstraint();

	/**
	 * @return {@code Constraint} if Qualification for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateQualificationConstraint();

	/**
	 * Indicates if the end user certificate used in validating the signature is mandated to be supported by a secure
	 * signature creation device (SSCD) as defined in Directive 1999/93/EC [9].
	 *
	 * @return {@code Constraint} if SupportedBySSCD for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateSupportedBySSCDConstraint();

	/**
	 * @return {@code Constraint} if IssuedToLegalPerson for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateIssuedToLegalPersonConstraint();

	/**
	 * @return {@code Constraint} if Recognition for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateRecognitionConstraint(String context);

	/**
	 * @return {@code Constraint} if Signed for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateSignedConstraint(String context);

	/**
	 * @return {@code Constraint} if SigningCertificateAttribute for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateAttributePresentConstraint(String context);

	/**
	 * @return {@code Constraint} if DigestValuePresent for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateDigestValuePresentConstraint(String context);

	/**
	 * @return {@code Constraint} if DigestValueMatch for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateDigestValueMatchConstraint(String context);

	/**
	 * @return {@code Constraint} if IssuerSerialMatch for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSigningCertificateIssuerSerialMatchConstraint(String context);

	/**
	 * @return {@code Constraint} if ReferenceDataExistence for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getReferenceDataExistenceConstraint();

	/**
	 * @return {@code Constraint} if ReferenceDataIntact for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getReferenceDataIntactConstraint();

	/**
	 * @return {@code Constraint} if SignatureIntact for a given context element is present in the constraint file, null otherwise.
	 */
	public abstract Constraint getSignatureIntactConstraint();

	/**
	 * This method returns the "basic" constraint able to handle simple (empty/not empty), boolean value and identifiers list.
	 *
	 * @param XP_ROOT              is the root part of the XPath query use to retrieve the constraint description.
	 * @param defaultExpectedValue true or false
	 * @return
	 */
	protected abstract Constraint getBasicConstraint(String XP_ROOT, boolean defaultExpectedValue);

	public abstract BasicValidationProcessValidConstraint getBasicValidationProcessConclusionConstraint();

	public abstract Constraint getMessageImprintDataFoundConstraint();

	public abstract Constraint getMessageImprintDataIntactConstraint();

	public abstract Constraint getRevocationTimeConstraint();

	public abstract Constraint getBestSignatureTimeBeforeIssuanceDateOfSigningCertificateConstraint();

	public abstract Constraint getSigningCertificateValidityAtBestSignatureTimeConstraint();

	public abstract Constraint getAlgorithmReliableAtBestSignatureTimeConstraint();

	public abstract Constraint getTimestampCoherenceConstraint();

	/**
	 * This constraint has only two levels: FAIL, or NOTHING
	 *
	 * @return
	 */
	public abstract Constraint getTimestampDelaySigningTimePropertyConstraint();

	public abstract ElementNumberConstraint getCounterSignatureNumberConstraint();

	/**
	 * This constraint is related to the global structure.
	 * It defines the number of required signatures.
	 *
	 * @return {@code ElementNumberConstraint}
	 */
	public abstract ElementNumberConstraint getSignatureNumberConstraint();

	/**
	 * This constraint is related to the global structure.
	 * It defines the number of VALID signatures.
	 *
	 * @return {@code ElementNumberConstraint}
	 */
	public abstract ElementNumberConstraint getValidSignatureNumberConstraint();

	public abstract ElementNumberConstraint getSignatureTimestampNumberConstraint();

	public abstract ElementNumberConstraint getManifestReferenceNumberConstraint();

	public abstract Constraint getManifestReferenceDataExistenceConstraint();

	public abstract Constraint getManifestReferenceIntactConstraint();

	public abstract List<Constraint> getISCCustomizedConstraints();

	public abstract Constraint getCompleteCertificateRefsConstraint();

	public abstract Constraint getCompleteRevocationRefsConstraint();

	public abstract ElementNumberConstraint getValidationDataTimestampNumberConstraint();

	public abstract ElementNumberConstraint getArchiveTimestampNumberConstraint();

	public abstract Constraint getCertificateValuesConstraint();

	public abstract Constraint getRevocationValuesConstraint();
}