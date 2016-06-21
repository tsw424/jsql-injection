/*******************************************************************************
 * Copyhacked (H) 2012-2014.
 * This program and the accompanying materials
 * are made available under no term at all, use it like
 * you want, but share and discuss about it
 * every time possible with every body.
 * 
 * Contributors:
 *      ron190 at ymail dot com - initial implementation
 ******************************************************************************/
package com.jsql.view.swing.manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.jsql.exception.PreparationException;
import com.jsql.exception.StoppableException;
import com.jsql.i18n.I18n;
import com.jsql.model.accessible.RessourceAccess;
import com.jsql.view.swing.HelperGui;
import com.jsql.view.swing.MediatorGui;
import com.jsql.view.swing.list.DnDList;
import com.jsql.view.swing.scrollpane.LightScrollPane;

/**
 * Manager to read a file from the host.
 */
@SuppressWarnings("serial")
public class ManagerFile extends ManagerAbstractList {
    /**
     * Log4j logger sent to view.
     */
    private static final Logger LOGGER = Logger.getLogger(ManagerFile.class);

    /**
     * Create the manager panel to read a file.
     */
    public ManagerFile() {
        this.setLayout(new BorderLayout());
        this.setDefaultText(I18n.FILE_RUN_BUTTON);
        
        List<String> pathList = new ArrayList<>();
        try {
            InputStream in = ManagerFile.class.getResourceAsStream("/com/jsql/view/swing/resources/list/file.txt");
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                pathList.add(line);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.error(e, e);
        }

        final DnDList listFile = new DnDList(pathList);

        this.add(new LightScrollPane(1, 1, 0, 0, listFile), BorderLayout.CENTER);

        JPanel lastLine = new JPanel();
        lastLine.setOpaque(false);
        lastLine.setLayout(new BoxLayout(lastLine, BoxLayout.X_AXIS));
        lastLine.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, HelperGui.COMPONENT_BORDER),
                BorderFactory.createEmptyBorder(1, 0, 1, 1)
            )
        );
        
        run = new JButton(defaultText, new ImageIcon(ManagerFile.class.getResource("/com/jsql/view/swing/resources/images/fileSearch.png")));

        run.setToolTipText(I18n.FILE_RUN_BUTTON_TOOLTIP);
        run.setEnabled(false);
        run.setBorder(HelperGui.BLU_ROUND_BORDER);
        
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (listFile.getSelectedValuesList().isEmpty()) {
                    LOGGER.warn("Select at least one file");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (run.getText().equals(defaultText)) {
                            run.setText("Stop");
                            try {
                                MediatorGui.tabManagers().shellManager.clearSelection();
                                MediatorGui.tabManagers().sqlShellManager.clearSelection();
                                loader.setVisible(true);
                                RessourceAccess.readFile(listFile.getSelectedValuesList());
                            } catch (PreparationException | StoppableException e) {
                                LOGGER.warn("Problem reading file", e);
                            }

                        } else {
                            RessourceAccess.endFileSearch = true;
                            run.setEnabled(false);
                        }
                    }
                }, "getFile").start();
            }
        });

        privilege = new JLabel(I18n.PRIVILEGE_LABEL, HelperGui.SQUARE_GREY, SwingConstants.LEFT);
        I18n.components.get("PRIVILEGE_LABEL").add(privilege);
        privilege.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, HelperGui.DEFAULT_BACKGROUND));
        privilege.setToolTipText(I18n.PRIVILEGE_TOOLTIP);

        loader.setVisible(false);

        lastLine.add(privilege);
        lastLine.add(Box.createHorizontalGlue());
        lastLine.add(loader);
        lastLine.add(Box.createRigidArea(new Dimension(5, 0)));
        lastLine.add(run);
        
        this.add(lastLine, BorderLayout.SOUTH);
    }
}