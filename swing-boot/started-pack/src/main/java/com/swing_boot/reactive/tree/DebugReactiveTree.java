package com.swing_boot.reactive.tree;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.CommonEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.components.colored.NodeSearchFrame;
import com.swing_boot.reactive.tree.components.colored.RandomColorFrame;
import com.swing_boot.reactive.tree.components.colored.RandomColorFrameCreator;
import com.swing_boot.reactive.tree.components.colored.AppRoot;
import com.swing_boot.reactive.tree.nodes.JFrameNode;
import com.swing_boot.reactive.tree.utils.PrintUtils;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Toolkit;

public class DebugReactiveTree {

    static final AppRoot APP_ROOT = new AppRoot(new JFrame(), "appRoot");

    public static void main(String[] args) {

        final int treeDepth = 2;

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        final RandomColorFrame testableFrame1 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        APP_ROOT.addChild(testableFrame1);
        testableFrame1.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame1.it.setLocation(0, 0);
        testableFrame1.it.setVisible(true);

        final RandomColorFrame testableFrame2 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        APP_ROOT.addChild(testableFrame2);
        testableFrame2.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame2.it.setLocation(screenSize.width / 2 , 0);
        testableFrame2.it.setVisible(true);

        /*final RandomColorFrame testableFrame3 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        APP_ROOT.addChild(testableFrame3);
        testableFrame3.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame3.it.setLocation(screenSize.width / 2,  screenSize.height / 2);
        testableFrame3.it.setVisible(true);

        final RandomColorFrame testableFrame4 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        APP_ROOT.addChild(testableFrame4);
        testableFrame4.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame4.it.setLocation(0, screenSize.height / 2);
        testableFrame4.it.setVisible(true);*/

        final JFrameNode<JFrame> searchFrame = NodeSearchFrame.instance();
        APP_ROOT.addChild(searchFrame);
        searchFrame.it.setVisible(true);

        APP_ROOT.addChild(
                new JFrameNode<JFrame>(new JFrame(), "appRootInfo") {
                    {
                        super.consumableEvents.addAll(Sets.newHashSet(CommonEvents.class));
                        SwingUtilities.invokeLater(() -> {
                                super.it.setTitle(super.name);
                                super.it.setSize(new Dimension(350, 10));
                                super.it.setAlwaysOnTop(true);
                                super.it.setVisible(true);
                            });
                    }

                    @Override
                    public void update(Events e, Object v) { super.it.setTitle(super.name + ": " + APP_ROOT.it.getTitle()); }
                });

        PrintUtils.printTreeBranch(APP_ROOT, "", true, true);
    }
}
