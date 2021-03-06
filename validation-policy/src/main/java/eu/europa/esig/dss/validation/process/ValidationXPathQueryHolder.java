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
package eu.europa.esig.dss.validation.process;

/**
 *
 */
public interface ValidationXPathQueryHolder {

	String XP_CONCLUSION = "./Conclusion";
	String XP_INDICATION = "./Indication/text()";
	String XP_CONCLUSION_INDICATION = XP_CONCLUSION + "/Indication/text()";
	String XP_SUB_INDICATION = "./SubIndication/text()";
	String XP_ERROR = "./Error";
	String XP_ERROR_NOT_BEFORE = "./Error/@NotBefore";
	String XP_ERROR_CERTIFICATE_ID = "./Error/@CertificateId";
	String XP_ERROR_REVOCATION_TIME = "./Error/@RevocationTime";
	String XP_WARNING = "./Warning";
	String XP_INFO = "./Info";

	String XP_NOT_BEFORE = "./NotBefore/text()";
	String XP_NOT_AFTER = "./NotAfter/text()";
	String XP_TRUSTED = "./Trusted/text()";
	String XP_MESSAGE_IMPRINT_DATA_FOUND = "./MessageImprintDataFound/text()";
	String XP_MESSAGE_IMPRINT_DATA_INTACT = "./MessageImprintDataIntact/text()";

	String XP_SIGNING_CERTIFICATE_ID = "./SigningCertificate/@Id";

	String XP_DIAGNOSTIC_DATA_SIGNATURE = "/DiagnosticData/Signature";

	String XP_BV_SIGNATURE_CONCLUSION = "/BasicValidationData/Signature[@Id='%s']/Conclusion";

	String XP_REFERENCE_DATA_FOUND = "./BasicSignature/ReferenceDataFound/text()";
	String XP_REFERENCE_DATA_INTACT = "./BasicSignature/ReferenceDataIntact/text()";
	String XP_SIGNATURE_INTACT = "./BasicSignature/SignatureIntact/text()";
	String XP_SIGNATURE_VALID = "./BasicSignature/SignatureValid/text()";
	String XP_ENCRYPTION_ALGO_USED_TO_SIGN_THIS_TOKEN = "./BasicSignature/EncryptionAlgoUsedToSignThisToken/text()";
	String XP_DIGEST_ALGO_USED_TO_SIGN_THIS_TOKEN = "./BasicSignature/DigestAlgoUsedToSignThisToken/text()";
	String XP_KEY_LENGTH_USED_TO_SIGN_THIS_TOKEN = "./BasicSignature/KeyLengthUsedToSignThisToken/text()";

	String XP_MANIFEST_ROOT = "./dss:BasicSignature/dss:References/dss:Reference[@Type='http://www.w3.org/2000/09/xmldsig#Manifest']";
	String XP_MANIFEST_REFERENCE_FOUND = "boolean(" + XP_MANIFEST_ROOT + ")";
	String XP_MANIFEST_REFERENCE = "/dss:ManifestReferences/dss:Reference";
	String XP_MANIFEST_REFERENCE_INTACT = XP_MANIFEST_ROOT + XP_MANIFEST_REFERENCE + "/dss:ReferenceDataIntact/text()";
	String XP_MANIFEST_REFERENCE_DATA_FOUND = XP_MANIFEST_ROOT + XP_MANIFEST_REFERENCE + "/dss:ReferenceDataFound/text()";
	String XP_MANIFEST_REFERENCE_URI = XP_MANIFEST_ROOT + XP_MANIFEST_REFERENCE + "/dss:Uri/text()";
	String XP_MANIFEST_REFERENCE_REAL_URI = XP_MANIFEST_ROOT + XP_MANIFEST_REFERENCE + "/dss:RealUri/text()";
	String XP_MANIFEST_DIGEST_ALGORITHM = XP_MANIFEST_ROOT + XP_MANIFEST_REFERENCE + "/dss:DigestMethod/text()";

	String XP_MANIFEST_REFERENCE_COUNT = "count(" + XP_MANIFEST_ROOT + ")";

	String XP_MANIFEST_CONSTRAINT = "boolean(/ConstraintsParameters/MainSignature/Manifest)";

	String XP_SIGNATURE = "./Signature[@Id='%s']";
	String XP_SIGNATURES = "./Signature";

	String XP_BBB_CONCLUSION = "./BasicBuildingBlocks/Conclusion";

	String XP_ADEST_SIGNATURE = "/AdESTValidationData/Signature[@Id='%s']";

	String XP_TVD_SIGNATURE_TIMESTAMP = "/TimestampValidationData/Signature[@Id='%s']/Timestamp[@Id='%s']";

	String XP_TIMESTAMP = "./Timestamp";
	String XP_TIMESTAMP_TYPE_CONCLUSION = "./Timestamp[@Type='%s']/Conclusion";
	String XP_TIMESTAMP_ID_CONCLUSION = "./Timestamp[@Id='%s']/Conclusion";
	String XP_TIMESTAMPS = "./Timestamps/Timestamp[@Type='%s']";
	String XP_TIMESTAMP_BBB_CONCLUSION = "./Timestamp[@Id='%s']/BasicBuildingBlocks/Conclusion";
	String XP_PRODUCTION_TIME = "./ProductionTime/text()";
	String XP_SIGNED_DATA_DIGEST_ALGO = "./SignedDataDigestAlgo/text()";
}
