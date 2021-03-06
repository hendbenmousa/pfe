/*
 * DSS - Digital Signature Services
 *
 * Copyright (C) 2013 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel
 *
 * Developed by: 2013 ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg) http://www.arhs-developments.com
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * "DSS - Digital Signature Services" is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * DSS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * "DSS - Digital Signature Services".  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.esig.dss.validation.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.XmlDom;
import eu.europa.esig.dss.validation.policy.ElementNumberConstraint;
import eu.europa.esig.dss.validation.policy.ProcessParameters;
import eu.europa.esig.dss.validation.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.policy.XmlNode;
import eu.europa.esig.dss.validation.policy.rules.ExceptionMessage;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.NodeName;
import eu.europa.esig.dss.validation.policy.rules.NodeValue;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.report.Conclusion;

import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_GS_DNSCVP;
import static eu.europa.esig.dss.validation.policy.rules.MessageTag.BBB_GS_DNSCVP_ANS;

/**
 *
 */
public class GeneralStructure extends BasicValidationProcess implements NodeName, NodeValue, Indication, SubIndication, ExceptionMessage {

	private static final Logger LOG = LoggerFactory.getLogger(GeneralStructure.class);

	private ValidationPolicy validationPolicy;
	private XmlNode generalStructureXmlNode;

	private void isInitialised(final ProcessParameters params) {

		assertDiagnosticData(params.getDiagnosticData(), getClass());
		validationPolicy = params.getValidationPolicy();
		assertValidationPolicy(validationPolicy, getClass());
	}

	/**
	 * This method lunches the validation process dedicated to the general structure constraints.
	 *
	 * @param params validation process parameters
	 * @return the {@code Conclusion} which indicates the result of the process
	 */
	public Conclusion run(final ProcessParameters params) {

		isInitialised(params);
		LOG.debug(this.getClass().getSimpleName() + ": start.");

		generalStructureXmlNode = new XmlNode(GENERAL_STRUCTURE_DATA);
		generalStructureXmlNode.setNameSpace(XmlDom.NAMESPACE);

		final Conclusion conclusion = process(params);

		conclusion.setValidationData(generalStructureXmlNode);
		params.setGeneralStructureConclusion(conclusion);
		return conclusion;
	}

	/**
	 * 5.3.4 Processing This process consists in the following steps:
	 *
	 * @param params validation process parameters
	 * @return
	 */
	private Conclusion process(final ProcessParameters params) {

		final Conclusion conclusion = new Conclusion();
		final String location = generalStructureXmlNode.getLocation();
		conclusion.setLocation(location);

		if (!checkSignatureNumberConstraint(conclusion, params.getDiagnosticData())) {
			return conclusion;
		}
		//      This check can be performed only after the signatures(s) validation
		//		if (!checkValidSignatureNumberConstraint(conclusion, context.getDiagnosticData())) {
		//			return conclusion;
		//		}
		// This validation process returns VALID
		conclusion.setIndication(VALID);
		return conclusion;
	}

	/**
	 * Check of number of signatures against the validation policy
	 * Even not valid signatures are taken into account.
	 *
	 * @param conclusion     the conclusion to use to add the result of the check.
	 * @param diagnosticData
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkSignatureNumberConstraint(final Conclusion conclusion, XmlDom diagnosticData) {

		final ElementNumberConstraint constraint = validationPolicy.getSignatureNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(generalStructureXmlNode, BBB_GS_DNSCVP);
		//		final long signatureCount = diagnosticData.getCountValue("/DiagnosticData/Signature[@Type!='COUNTERSIGNATURE']");
		final long signatureCount = diagnosticData.getCountValue("count(/DiagnosticData/Signature[not(@Type)])");
		constraint.setIntValue((int) signatureCount);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_GS_DNSCVP_ANS);
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.check();
		return check;
	}

	/**
	 * Check of number of VALID signatures against the validation policy
	 *
	 * @param conclusion     the conclusion to use to add the result of the check.
	 * @param diagnosticData
	 * @return false if the check failed and the process should stop, true otherwise.
	 */
	private boolean checkValidSignatureNumberConstraint(final Conclusion conclusion, XmlDom diagnosticData) {

		final ElementNumberConstraint constraint = validationPolicy.getSignatureNumberConstraint();
		if (constraint == null) {
			return true;
		}
		constraint.create(generalStructureXmlNode, BBB_GS_DNSCVP);
		//		final long signatureCount = diagnosticData.getCountValue("/DiagnosticData/Signature[@Type!='COUNTERSIGNATURE']");
		final long signatureCount = diagnosticData.getCountValue("count(/DiagnosticData/Signature[not(@Type)])");
		constraint.setIntValue((int) signatureCount);
		constraint.setIndications(INVALID, SIG_CONSTRAINTS_FAILURE, BBB_GS_DNSCVP_ANS);
		constraint.setConclusionReceiver(conclusion);
		boolean check = constraint.check();
		return check;
	}
}
