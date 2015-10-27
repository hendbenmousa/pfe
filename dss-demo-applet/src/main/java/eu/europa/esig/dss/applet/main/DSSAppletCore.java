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
package eu.europa.esig.dss.applet.main;

import eu.europa.esig.dss.applet.controller.ActivityController;
import eu.europa.esig.dss.applet.model.ActivityModel;
import eu.europa.esig.dss.applet.model.SignatureModel;
import eu.europa.esig.dss.applet.swing.mvc.AppletCore;
import eu.europa.esig.dss.applet.wizard.signature.SignatureWizardController;

/**
 *
 */
@SuppressWarnings("serial")
public class DSSAppletCore extends AppletCore {

	private Parameters parameters;

	/**
	 * @return {@code Parameters}
	 */
	public Parameters getParameters() {
		return parameters;
	}

	@Override
	protected void layout(final AppletCore core) {
		getController(ActivityController.class).display();
	}

	@Override
	protected void registerControllers() {
		getControllers().put(ActivityController.class, new ActivityController(this, new ActivityModel()));
		getControllers().put(SignatureWizardController.class, new SignatureWizardController(this, new SignatureModel()));
	}

	@Override
	protected void registerParameters(final ParameterProvider parameterProvider) {

		LOG.info("Register applet parameters ");
		final String setupPath = parameterProvider.getParameter("setup-path");

		final Parameters parameters = new Parameters(setupPath);

//			final String signaturePackagingParam = parameterProvider.getParameter(PARAM_SIGNATURE_PACKAGING);
//			if (StringUtils.isNotEmpty(signaturePackagingParam)) {
//				parameters.setPackaging(SignaturePackaging.valueOf(signaturePackagingParam));
//				final String signatureLevelParam = parameterProvider.getParameter(PARAM_SIGNATURE_LEVEL);
//				if (StringUtils.isNotEmpty(signatureLevelParam)) {
//					parameters.setSignatureLevel(signatureLevelParam);
//				}
//			}
//		}
//
//		// Signature Token
//		final String tokenParam = parameterProvider.getParameter(PARAM_TOKEN_TYPE);
//		if (DSSStringUtils.contains(tokenParam, SignatureTokenType.MSCAPI.name(), SignatureTokenType.PKCS11.name(), SignatureTokenType.PKCS12.name())) {
//			parameters.setSignatureTokenType(SignatureTokenType.valueOf(tokenParam));
//		} else {
//			LOG.warn("Invalid value of " + PARAM_TOKEN_TYPE + " parameter: {}", tokenParam);
//		}
//
//		// File path PKCS11
//		final String pkcs11Param = parameterProvider.getParameter(PARAM_PKCS11_FILE);
//		if (StringUtils.isNotEmpty(pkcs11Param)) {
//			final File file = new File(pkcs11Param);
//			if (!file.exists() || file.isFile()) {
//				LOG.warn("Invalid value of " + PARAM_PKCS11_FILE + " parameter: {}", pkcs11Param);
//			}
//			parameters.setPkcs11File(file);
//		}
//
//		// File path PKCS12
//		final String pkcs12Param = parameterProvider.getParameter(PARAM_PKCS12_FILE);
//		if (StringUtils.isNotEmpty(pkcs12Param)) {
//			final File file = new File(pkcs12Param);
//			if (!file.exists() || file.isFile()) {
//				LOG.warn("Invalid value of " + PARAM_PKCS12_FILE + " parameter: {}", pkcs11Param);
//			}
//			parameters.setPkcs12File(file);
//		}
//
//		final String signaturePolicyAlgoParam = parameterProvider.getParameter(PARAM_SIGNATURE_POLICY_ALGO);
//		parameters.setSignaturePolicyAlgo(signaturePolicyAlgoParam);
//
//		final String signaturePolicyValueParam = parameterProvider.getParameter(PARAM_SIGNATURE_POLICY_HASH);
//		parameters.setSignaturePolicyValue(Base64.decodeBase64(signaturePolicyValueParam));
//
//		// Default policy URL
//		final String defaultPolicyUrl = parameterProvider.getParameter(PARAM_DEFAULT_POLICY_URL);
//		if (StringUtils.isNotEmpty(defaultPolicyUrl)) {
//			try {
//				parameters.setDefaultPolicyUrl(new URL(defaultPolicyUrl));
//			} catch (IOException e) {
//				throw new IllegalArgumentException(PARAM_DEFAULT_POLICY_URL + " cannot be opened", e);
//			}
//		}

		this.parameters = parameters;

		LOG.info("Parameters - {}", parameters);
	}
}
