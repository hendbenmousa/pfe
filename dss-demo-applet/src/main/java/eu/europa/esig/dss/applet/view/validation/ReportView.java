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
package eu.europa.esig.dss.applet.view.validation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import eu.europa.esig.dss.DSSXMLUtils;
import eu.europa.esig.dss.applet.main.Parameters;
import eu.europa.esig.dss.applet.model.ValidationModel;
import eu.europa.esig.dss.applet.swing.mvc.AppletCore;
import eu.europa.esig.dss.applet.swing.mvc.wizard.WizardView;
import eu.europa.esig.dss.applet.util.ComponentFactory;
import eu.europa.esig.dss.applet.util.XsltConverter;
import eu.europa.esig.dss.applet.wizard.validation.ValidationWizardController;
import eu.europa.esig.dss.validation.report.SimpleReport;

/**
 * TODO
 */
public class ReportView extends WizardView<ValidationModel, ValidationWizardController> {

	private final ValueHolder simpleReportValueHolder;
	private JTextArea simpleReportText;

	/**
	 * The default constructor for ReportView.
	 *
	 * @param core
	 * @param controller
	 * @param model
	 */
	public ReportView(final AppletCore core, final ValidationWizardController controller, final ValidationModel model) {

		super(core, controller, model);
		simpleReportValueHolder = new ValueHolder("");

		simpleReportText = ComponentFactory.createTextArea(simpleReportValueHolder);
		simpleReportText.setTabSize(2);
	}

	@Override
	public void doInit() {

		final ValidationModel model = getModel();

		final SimpleReport simpleReport = model.getSimpleReport();
		final String simpleReportString = simpleReport.toString();
		simpleReportValueHolder.setValue(simpleReportString);

		//		final XmlDom detailedReport = model.getDetailedReport();
		//		final String reportText = detailedReport.toString();
		//		detailedReportValueHolder.setValue(reportText);
		//
		//		final XMLTreeModel xmlTreeModelReport = new XMLTreeModel();
		//		Element doc = detailedReport.getRootElement();
		//		xmlTreeModelReport.setDocument(doc);
		//
		//		final XmlDom diagnosticData = model.getDiagnosticData();
		//		final Document document = diagnosticData.getRootElement().getOwnerDocument();
		//		final XMLTreeModel xmlTreeModelDiagnostic = new XMLTreeModel();
		//		xmlTreeModelDiagnostic.setDocument(document.getDocumentElement());
		//		diagnostic = ComponentFactory.tree("Diagnostic", xmlTreeModelDiagnostic);
		//		expandTree(diagnostic);
		//
		//		diagnosticValueHolder.setValue(diagnosticData.toString());
		//
		//		final Document simpleReportHtml = getController().renderSimpleReportAsHtml();
		//		simpleReportHtmlPanel.setDocument(simpleReportHtml);
		//
		//		final Document detailedReportHtml = getController().renderValidationReportAsHtml();
		//		detailedReportHtmlPanel.setDocument(detailedReportHtml);
	}

	@Override
	protected Container doLayout() {

		//		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		final JPanel simpleReportText = getSimpleReportText();
		//		tabbedPane.addTab("Simple Report XML", simpleReportText);
		return simpleReportText;
	}

	//	private JPanel getHtmlPanel(final String textWithMnemonic) {
	//
	//		final String[] columnSpecs = new String[]{"5dlu", "pref", "5dlu", "fill:default:grow", "5dlu"};
	//		final String[] rowSpecs = new String[]{"5dlu", "pref", "5dlu", "fill:default:grow", "5dlu", "pref", "5dlu"};
	//		final PanelBuilder builder = ComponentFactory.createBuilder(columnSpecs, rowSpecs);
	//		final CellConstraints cc = new CellConstraints();
	//
	//		builder.addSeparator(textWithMnemonic, cc.xyw(2, 2, 3));
	//		scrollPane = ComponentFactory.createScrollPane(simpleReportText);
	//		builder.add(scrollPane, cc.xyw(2, 4, 3));
	//
	//		return ComponentFactory.createPanel(builder);
	//	}
	//
	private JPanel getSimpleReportText() {

		final String[] columnSpecs = new String[]{"5dlu", "fill:default:grow", "5dlu"};
		final String[] rowSpecs = new String[]{"5dlu", "pref", "5dlu", "fill:default:grow", "5dlu", "pref", "5dlu"};
		final PanelBuilder builder = ComponentFactory.createBuilder(columnSpecs, rowSpecs);
		final CellConstraints cc = new CellConstraints();

		builder.addSeparator("Simple Validation Report XML", cc.xyw(2, 2, 1));
		final JScrollPane scrollPane = ComponentFactory.createScrollPane(simpleReportText);
		builder.add(scrollPane, cc.xyw(2, 4, 1));
		builder.add(ComponentFactory.createSaveButton("Open Validation report", true, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				try {
					final Parameters parameter = getController().getParameter();
					final String outPath = parameter.getValidationOutPath();
					final FileOutputStream htmlOutputStream = new FileOutputStream(outPath);
					final SimpleReport simpleReportXmlDom = getModel().getSimpleReport();
					final String xsltPath = "/simpleReport.xslt";
					final Document document = XsltConverter.renderAsHtml(simpleReportXmlDom, xsltPath, null);
					DSSXMLUtils.transform(document, htmlOutputStream);
					IOUtils.closeQuietly(htmlOutputStream);
					System.out.println("Transformation done: " + outPath);
					final File htmlFile = new File(outPath);
					Desktop.getDesktop().browse(htmlFile.toURI());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}), cc.xyw(2, 6, 1));

		return ComponentFactory.createPanel(builder);
	}
}