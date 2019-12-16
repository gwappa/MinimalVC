/*
 * MIT License
 *
 * Copyright (c) 2019-2020 Keisuke Sehara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

// may not needed in the future
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.JList;

import cc.chaos.vc.git.GitRepository;

/**
 *  the main UI.
 *  @author gwappa
 */
public class MinimalVC
    extends JPanel
{
    static final String TITLE_EMPTY = "(MinimalVC) <no repository selected>";

    JSplitPane content_;

    public MinimalVC()
    {
        super(new BorderLayout());
        setupUI();
    }

    /**
     *  initializes itself with the default layout.
     */
    private void setupUI()
    {
        // add tree view for directory view
        JTree directoryView         = new JTree(new GitRepository(new java.io.File(System.getProperty("user.dir"))));

        // right side will be the control panel
        JPanel controlPanel         = new JPanel();

        GridBagLayout       layout  = new GridBagLayout();
        GridBagConstraints  cons    = new GridBagConstraints();
        controlPanel.setLayout(layout);

        cons.weightx = 1.0;
        cons.gridx   = 1;
        cons.fill    = GridBagConstraints.BOTH;

        // add unstaged changes view
        JPanel unstagedChangesView  = new JPanel();
        unstagedChangesView.setBorder(
            BorderFactory.createTitledBorder("Unstaged changes"));
        cons.gridheight = 1;
        cons.weighty    = 1.0;
        layout.setConstraints(unstagedChangesView, cons);
        controlPanel.add(unstagedChangesView);

        // add staged changes view
        JPanel stagedChangesView    = new JPanel();
        stagedChangesView.setBorder(
            BorderFactory.createTitledBorder("Staged changes"));
        layout.setConstraints(stagedChangesView, cons);
        controlPanel.add(stagedChangesView);

        // add commit message field
        JTextField commitMessageField   = new JTextField();
        commitMessageField.setBorder(
            BorderFactory.createTitledBorder("Commit to repository"));
        cons.weighty = 0.5;
        layout.setConstraints(commitMessageField, cons);
        controlPanel.add(commitMessageField);

        // add commit log
        JPanel commitLogView            = new JPanel();
        commitLogView.setBorder(
            BorderFactory.createTitledBorder("Commit log"));
        cons.weighty = 1.0;
        layout.setConstraints(commitLogView, cons);
        controlPanel.add(commitLogView);

        content_ = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                directoryView,
                                controlPanel);
        add(content_, BorderLayout.CENTER);
    }

    public void resetVerticalSplit() {
        content_.setDividerLocation(0.5);
    }

    public static void main(String[] args)
    {
        MinimalVC content = new MinimalVC();
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setLocation(100, 100);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(TITLE_EMPTY);
        frame.setVisible(true);
        content.resetVerticalSplit();
    }
}
