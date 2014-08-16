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
package com.jsql.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.jsql.model.InjectionModel;
import com.jsql.view.ToolsGUI;

/**
 * Open another jSQL instance in new process. 
 */
@SuppressWarnings("serial")
public class ActionNewWindow extends AbstractAction {
    /**
     * Log4j logger sent to view.
     */
    private static final Logger LOGGER = Logger.getLogger(ActionNewWindow.class);

    public ActionNewWindow() {
        super();

        this.putValue(Action.NAME, "New Window");
        this.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        this.putValue(Action.SMALL_ICON, ToolsGUI.EMPTY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.info("Starting new window.");
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home") + separator + "bin" + separator + "java";
        ProcessBuilder processBuilder = 
                new ProcessBuilder(path, "-cp", classpath, InjectionModel.class.getName());
        try {
            processBuilder.start();
        } catch (IOException e1) {
            LOGGER.error("Error opening new window.");
        }        
    }
}