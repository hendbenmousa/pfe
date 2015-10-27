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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.applet.SignatureTokenType;
import eu.europa.esig.dss.signature.SignaturePackaging;
import eu.europa.esig.dss.validation.ValidationResourceManager;
import eu.europa.esig.dss.x509.SignatureForm;

import static eu.europa.esig.dss.applet.SignatureTokenType.MSCAPI;
import static eu.europa.esig.dss.applet.SignatureTokenType.PKCS11;
import static eu.europa.esig.dss.applet.SignatureTokenType.PKCS12;
import static eu.europa.esig.dss.applet.main.Parameters.AppletUsage.SIGN;
import static eu.europa.esig.dss.applet.main.Parameters.AppletUsage.VALIDATE;
import static eu.europa.esig.dss.applet.main.Parameters.Level.B;
import static eu.europa.esig.dss.applet.main.Parameters.Level.LT;
import static eu.europa.esig.dss.applet.main.Parameters.Level.LTA;
import static eu.europa.esig.dss.applet.main.Parameters.Level.T;
import static eu.europa.esig.dss.signature.SignaturePackaging.DETACHED;
import static eu.europa.esig.dss.signature.SignaturePackaging.ENVELOPED;
import static eu.europa.esig.dss.signature.SignaturePackaging.ENVELOPING;
import static eu.europa.esig.dss.x509.SignatureForm.CAdES;
import static eu.europa.esig.dss.x509.SignatureForm.PAdES;
import static eu.europa.esig.dss.x509.SignatureForm.XAdES;

/**
 * TODO
 */
public class Parameters {

	public static final String USAGE = "USAGE";
	public static final String FORM = "FORMAT";
	public static final String LEVEL = "LEVEL";
	public static final String PACKAGING = "PACKAGING";
	public static final String TOKEN = "TOKEN";
	public static final String PKCS11_FILE = "PKCS11_FILE";
	public static final String PKCS12_FILE = "PKCS12_FILE";
	public static final String SIGNATURE_HASH = "SIGNATURE_HASH";
	public static final String TIMESTAMP_HASH = "TIMESTAMP_HASH";
	public static final String VALIDATION_POLICY = "VALIDATION_POLICY";

	/**
	 *
	 */
	private List<AppletUsage> usageList = new ArrayList<AppletUsage>() {{
		add(SIGN);
		add(VALIDATE);
	}};
	/**
	 *
	 */
	private List<SignatureForm> formList = new ArrayList<SignatureForm>() {{
		add(XAdES);
		add(CAdES);
		add(PAdES);
	}};
	/**
	 *
	 */
	private List<Level> levelList = new ArrayList<Level>() {{
		add(B);
		add(T);
		add(LT);
		add(LTA);
	}};
	/**
	 *
	 */
	private List<SignaturePackaging> packagingList = new ArrayList<SignaturePackaging>() {{
		add(DETACHED);
		add(ENVELOPED);
		add(ENVELOPING);
	}};
	/**
	 *
	 */
	private List<SignatureTokenType> tokenTypeList = new ArrayList<SignatureTokenType>() {{
		add(PKCS11);
		add(PKCS12);
		add(MSCAPI);
	}};
	/**
	 *
	 */
	private File pkcs11File;
	/**
	 *
	 */
	private File pkcs12File;

	/**
	 *
	 */
	private DigestAlgorithm signatureHashAlgorithm = DigestAlgorithm.SHA256;

	/**
	 *
	 */
	private DigestAlgorithm timestampHashAlgorithm = DigestAlgorithm.SHA1;

	/**
	 *
	 */
	private String signaturePolicyAlgorithm;
	/**
	 *
	 */
	private byte[] signaturePolicyValue;
	/**
	 *
	 */
	private URL defaultPolicyUrl;

	/**
	 * The default constructor for Parameters.
	 *
	 * @param setupPath
	 */
	public Parameters(final String setupPath) {

		try {

			final InputStream inputStream = DSSUtils.toInputStream(setupPath);
			final Properties properties = new Properties();
			properties.load(inputStream);

			initUsage(properties);
			initForm(properties);
			initLevel(properties);
			initPackaging(properties);
			initTokenType(properties);
			initSignatureHashAlgorithm(properties);

		} catch (IOException e) {
			throw new DSSException(e);
		}
	}

	private void initUsage(final Properties properties) {

		final String usage = properties.getProperty(USAGE);
		final String[] splitArray = usage.split("|");
		final List appletUsageList = new ArrayList();
		for (final String split : splitArray) {

			if (StringUtils.isNotEmpty(split)) {

				final AppletUsage appletUsage = AppletUsage.valueOf(split.toUpperCase());
				appletUsageList.add(appletUsage);
			}
		}
		if (!appletUsageList.isEmpty()) {
			setUsageList(appletUsageList);
		}
	}

	private void initForm(final Properties properties) {

		final String form = properties.getProperty(FORM);
		final String[] splitArray = form.split("|");
		final List formList = new ArrayList();
		for (final String split : splitArray) {

			if (StringUtils.isNotEmpty(split)) {

				final SignatureForm signatureForm = SignatureForm.valueOf(split);
				formList.add(signatureForm);
			}
		}
		if (!formList.isEmpty()) {
			setFormList(formList);
		}
	}

	private void initLevel(final Properties properties) {

		final String form = properties.getProperty(LEVEL);
		final String[] splitArray = form.split("|");
		final List levelList = new ArrayList();
		for (final String split : splitArray) {

			if (StringUtils.isNotEmpty(split)) {

				final Level level = Level.valueOf(split);
				levelList.add(level);
			}
		}
		if (!levelList.isEmpty()) {
			setLevelList(levelList);
		}
	}

	private void initPackaging(final Properties properties) {

		final String packaging = properties.getProperty(PACKAGING);
		final String[] splitArray = packaging.split("|");
		final List packagingList = new ArrayList();
		for (final String split : splitArray) {

			if (StringUtils.isNotEmpty(split)) {

				final Level level = Level.valueOf(split);
				packagingList.add(level);
			}
		}
		if (!packagingList.isEmpty()) {
			setPackagingList(packagingList);
		}
	}

	private void initTokenType(final Properties properties) {

		final String token = properties.getProperty(TOKEN);
		final String[] splitArray = token.split("|");
		final List tokenTypeList = new ArrayList();
		for (final String split : splitArray) {

			if (StringUtils.isNotEmpty(split)) {

				final Level level = Level.valueOf(split);
				tokenTypeList.add(level);
			}
		}
		if (!tokenTypeList.isEmpty()) {
			setTokenTypeList(tokenTypeList);
		}
	}

	private void initSignatureHashAlgorithm(final Properties properties) {

		final String token = properties.getProperty(SIGNATURE_HASH);
		DigestAlgorithm signatureHashAlgorithm = DigestAlgorithm.forName(token);
	}

	public List<AppletUsage> getUsageList() {
		return usageList;
	}

	public void setUsageList(List<AppletUsage> usageList) {
		this.usageList = usageList;
	}

	/**
	 * @return the pkcs11File
	 */
	public File getPkcs11File() {
		return pkcs11File;
	}

	/**
	 * @param pkcs11File the pkcs11File to set
	 */
	public void setPkcs11File(final File pkcs11File) {
		this.pkcs11File = pkcs11File;
	}

	/**
	 * @return the pkcs12File
	 */
	public File getPkcs12File() {
		return pkcs12File;
	}

	/**
	 * @param pkcs12File the pkcs12File to set
	 */
	public void setPkcs12File(final File pkcs12File) {
		this.pkcs12File = pkcs12File;
	}

	public List<SignatureForm> getFormList() {
		return formList;
	}

	public void setFormList(List formList) {
		this.formList = formList;
	}

	public List<Level> getLevelList() {
		return levelList;
	}

	public void setLevelList(List signatureLevelList) {
		this.levelList = signatureLevelList;
	}

	public List<SignaturePackaging> getPackagingList() {
		return packagingList;
	}

	public void setPackagingList(List packagingList) {
		this.packagingList = packagingList;
	}

	/**
	 * @return the signaturePolicyAlgorithm
	 */
	public String getSignaturePolicyAlgorithm() {
		return signaturePolicyAlgorithm;
	}

	/**
	 * @param signaturePolicyAlgorithm the signaturePolicyAlgorithm to set
	 */
	public void setSignaturePolicyAlgorithm(final String signaturePolicyAlgorithm) {
		this.signaturePolicyAlgorithm = signaturePolicyAlgorithm;
	}

	/**
	 * @return the signaturePolicyValue
	 */
	public byte[] getSignaturePolicyValue() {
		if (signaturePolicyValue == null) {
			signaturePolicyValue = DSSUtils.EMPTY_BYTE_ARRAY;
		}
		return signaturePolicyValue;
	}

	/**
	 * @param signaturePolicyValue the signaturePolicyValue to set
	 */
	public void setSignaturePolicyValue(final byte[] signaturePolicyValue) {
		this.signaturePolicyValue = signaturePolicyValue;
	}

	/**
	 * @return the signatureTokenType
	 */
	public List<SignatureTokenType> getTokenTypeList() {
		return tokenTypeList;
	}

	/**
	 * @param tokenTypeList the tokenTypeList to set
	 */
	public void setTokenTypeList(final List<SignatureTokenType> tokenTypeList) {
		this.tokenTypeList = tokenTypeList;
	}

	/**
	 * @return
	 */
	public boolean hasPkcs11File() {
		final File file = getPkcs11File();
		return (file != null) && file.exists() && file.isFile();
	}

	/**
	 * @return
	 */
	public boolean hasPkcs12File() {
		final File file = getPkcs12File();
		return (file != null) && file.exists() && file.isFile();
	}

	/**
	 * @return
	 */
	public boolean hasSignaturePolicyAlgorithm() {
		return StringUtils.isNotEmpty(signaturePolicyAlgorithm);
	}

	/**
	 * @return
	 */
	public boolean hasSignaturePolicyValue() {
		return getSignaturePolicyValue().length != 0;
	}

	/**
	 * @return
	 */
	public boolean hasSignatureTokenType() {
		return !tokenTypeList.isEmpty();
	}

	/**
	 * @return the defaultPolicyUrl for validation. Can be null.
	 */
	public URL getDefaultPolicyUrl() {
		if (defaultPolicyUrl == null) {
			return getClass().getResource(ValidationResourceManager.defaultPolicyConstraintsLocation);
		} else {
			return defaultPolicyUrl;
		}
	}

	/**
	 * Set the default policy URL for validation. Can be null.
	 *
	 * @param defaultPolicyUrl
	 */
	public void setDefaultPolicyUrl(URL defaultPolicyUrl) {
		this.defaultPolicyUrl = defaultPolicyUrl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

	public enum AppletUsage {
		SIGN, VALIDATE
	}

	public enum Level {
		B, T, LT, LTA
	}
}