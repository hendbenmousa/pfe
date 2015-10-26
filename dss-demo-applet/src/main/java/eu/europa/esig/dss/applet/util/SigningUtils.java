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
package eu.europa.esig.dss.applet.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.AbstractSignatureParameters;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.SignatureTokenConnection;

/**
 * TODO
 *
 *
 *
 *
 *
 *
 */
public final class SigningUtils {

	private static final Logger logger = LoggerFactory.getLogger(SigningUtils.class);

//	private static ObjectFactory FACTORY;
//
//	static {
//		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
//		FACTORY = new ObjectFactory();
//	}

	private SigningUtils() {
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws DSSException
	 */
	public static DSSDocument signDocument(final File file, final AbstractSignatureParameters params, DSSPrivateKeyEntry privateKey, SignatureTokenConnection tokenConnection) throws DSSException {

//		try {
//
//			final WsDocument wsDocument = toWsDocument(file);
//
//			SignatureService_Service.setROOT_SERVICE_URL(serviceURL);
//			final SignatureService_Service signatureService_service = new SignatureService_Service();
//			final SignatureService signatureServiceImplPort = signatureService_service.getSignatureServiceImplPort();
//
//			final byte[] toBeSignedBytes = signatureServiceImplPort.getDataToSign(wsDocument, wsParameters);
//
//			DigestAlgorithm wsDigestAlgo = wsParameters.getDigestAlgorithm();
//			final byte[] encrypted = tokenConnection.sign(toBeSignedBytes, eu.europa.esig.dss.DigestAlgorithm.forName(wsDigestAlgo.name()), privateKey);
//
//			final WsDocument wsSignedDocument = signatureServiceImplPort.signDocument(wsDocument, wsParameters, encrypted);
//
//			final InMemoryDocument inMemoryDocument = toInMemoryDocument(wsSignedDocument);
//			return inMemoryDocument;
//		} catch (Exception e) {
//			throw new DSSException(e);
//		}
		return null;
	}

}
