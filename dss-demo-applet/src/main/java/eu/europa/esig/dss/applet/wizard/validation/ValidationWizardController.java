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
package eu.europa.esig.dss.applet.wizard.validation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.applet.controller.ActivityController;
import eu.europa.esig.dss.applet.controller.DSSWizardController;
import eu.europa.esig.dss.applet.main.DSSAppletCore;
import eu.europa.esig.dss.applet.main.Parameters;
import eu.europa.esig.dss.applet.model.ValidationModel;
import eu.europa.esig.dss.applet.swing.mvc.wizard.WizardController;
import eu.europa.esig.dss.applet.swing.mvc.wizard.WizardStep;
import eu.europa.esig.dss.applet.view.validation.ReportView;
import eu.europa.esig.dss.applet.view.validation.ValidationView;

/**
 * TODO
 */
public class ValidationWizardController extends DSSWizardController<ValidationModel> {

	private ReportView reportView;

	private ValidationView formView;

	/**
	 * The default constructor for ValidationWizardController.
	 *
	 * @param core
	 * @param model
	 */
	public ValidationWizardController(final DSSAppletCore core, final ValidationModel model) {

		super(core, model);
		final Parameters parameters = core.getParameters();
	}

	/**
	 *
	 */
	public void displayFormView() {

		formView.show();
	}

	@Override
	protected void doCancel() {
		getCore().getController(ActivityController.class).display();
	}

	@Override
	protected Class<? extends WizardStep<ValidationModel, ? extends WizardController<ValidationModel>>> doStart() {
		return FormStep.class;
	}

	@Override
	protected void registerViews() {

		formView = new ValidationView(getCore(), this, getModel());
		reportView = new ReportView(getCore(), this, getModel());
	}

	@Override
	protected Map<Class<? extends WizardStep<ValidationModel, ? extends WizardController<ValidationModel>>>, ? extends WizardStep<ValidationModel, ? extends WizardController<ValidationModel>>> registerWizardStep() {

		final Map steps = new HashMap();
		steps.put(FormStep.class, new FormStep(getModel(), formView, this));
		steps.put(ReportStep.class, new ReportStep(getModel(), reportView, this));
		return steps;
	}

	/**
	 * Validate the document with the 102853 validation policy
	 *
	 * @throws IOException
	 */
	public void validateDocument() throws DSSException {

		final ValidationModel model = getModel();

		final File signedFile = model.getSignedFile();
		//		final WsDocument wsSignedDocument = toWsDocument(signedFile);
		//
		//		final File detachedFile = model.getOriginalFile();
		//		final WsDocument wsDetachedDocument = detachedFile != null ? toWsDocument(detachedFile) : null;
		//
		//		WsDocument wsPolicyDocument = null;
		//		if (!model.isDefaultPolicy() && model.getSelectedPolicyFile() != null) {
		//
		//			final File policyFile = new File(model.getSelectedPolicyFile().getAbsolutePath());
		//			final InputStream inputStream = DSSUtils.toInputStream(policyFile);
		//			wsPolicyDocument = new WsDocument();
		//			wsPolicyDocument.setBytes(DSSUtils.toByteArray(inputStream));
		//		}
		//
		//		//assertValidationPolicyFileValid(validationPolicyURL);
		//
		//		final ValidationService_Service validationService_service = new ValidationService_Service();
		//		final ValidationService validationServiceImplPort = validationService_service.getValidationServiceImplPort();
		//		final WsValidationReport wsValidationReport;
		//		try {
		//			wsValidationReport = validationServiceImplPort.validateDocument(wsSignedDocument, wsDetachedDocument, wsPolicyDocument, true);
		//		} catch (DSSException_Exception e) {
		//			throw new DSSException(e);
		//		} catch (Throwable e) {
		//			throw new DSSException(e);
		//		}
		//
		//		String xmlData = "";
		//		try {
		//
		//			// In case of some signatures, the returned data are not UTF-8 encoded. The conversion is forced.
		//
		//			xmlData = wsValidationReport.getXmlDiagnosticData();
		//			// final String xmlDiagnosticData = DSSUtils.getUtf8String(xmlData);
		//			final XmlDom diagnosticDataXmlDom = getXmlDomReport(xmlData);
		//			model.setDiagnosticData(diagnosticDataXmlDom);
		//
		//			xmlData = "";
		//			xmlData = wsValidationReport.getXmlDetailedReport();
		//			// final String xmlDetailedReport = DSSUtils.getUtf8String(xmlData);
		//			final XmlDom detailedReportXmlDom = getXmlDomReport(xmlData);
		//			model.setDetailedReport(detailedReportXmlDom);
		//
		//			xmlData = "";
		//			xmlData = wsValidationReport.getXmlSimpleReport();
		//			// final String xmlSimpleReport = DSSUtils.getUtf8String(xmlData);
		//			final XmlDom simpleReportXmlDom = getXmlDomReport(xmlData);
		//			model.setSimpleReport(simpleReportXmlDom);
		//		} catch (Exception e) {
		//
		//			final String base64Encode = DSSUtils.base64Encode(xmlData.getBytes());
		//			LOG.error("Erroneous data: " + base64Encode);
		//			if (e instanceof DSSException) {
		//				throw (DSSException) e;
		//			}
		//			throw new DSSException(e);
		//		}
	}
}