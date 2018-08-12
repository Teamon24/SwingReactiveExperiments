package com.swing_boot.reactive.tree;

import com.google.common.collect.Lists;
import com.swing_boot.reactive.tree.components.colored.NodeSearchFrame;
import com.swing_boot.reactive.tree.components.colored.RandomColorButton;
import com.swing_boot.reactive.tree.components.colored.RandomColorFrame;
import com.swing_boot.reactive.tree.components.colored.RandomColorFrameCreator;
import com.swing_boot.reactive.tree.nodes.AppRootNode;
import com.swing_boot.reactive.tree.nodes.JFrameNode;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.tree.utils.PrintUtils;
import com.swing_boot.reactive.tree.utils.TreeUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SwingReactiveTree {

    static final AppRootNode appRootNode =
            new AppRootNode(new JPanel(), "appRoot");

    public static void main(String[] args) {
        final int treeDepth = 2;

        final RandomColorFrame testableFrame1 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        final RandomColorFrame testableFrame2 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        final RandomColorFrame testableFrame3 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        final RandomColorFrame testableFrame4 = RandomColorFrameCreator.createTestableFrame(treeDepth);
        final JFrameNode<JFrame> searchFrame = NodeSearchFrame.instance();

        appRootNode.addChild(testableFrame1);
        appRootNode.addChild(testableFrame2);
        appRootNode.addChild(testableFrame3);
        appRootNode.addChild(testableFrame4);
        appRootNode.addChild(searchFrame);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        testableFrame1.it.setLocation(0,                     0);
        testableFrame2.it.setLocation(screenSize.width / 2 , 0);
        testableFrame3.it.setLocation(screenSize.width / 2,  screenSize.height / 2);
        testableFrame4.it.setLocation(0,                     screenSize.height / 2);

        testableFrame1.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame2.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame3.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        testableFrame4.it.setSize(new Dimension(screenSize.width/2, screenSize.height/2));

        testableFrame1.it.setVisible(true);
        testableFrame2.it.setVisible(true);
        testableFrame3.it.setVisible(true);
        testableFrame4.it.setVisible(true);
        searchFrame.it.setVisible(true);

        final RandomColorButton randomColorButton = new RandomColorButton();
        appRootNode.addChild(randomColorButton);

        PrintUtils.printTreeBranch(appRootNode, "", true, true);

        final List<String> allNodeNames = TreeUtils.getNodeNamesOf(appRootNode);
        final ArrayList<String> filters = Lists.newArrayList("dispatchUp", "dispatchDown");
        filters.addAll(allNodeNames);
        LogUtils.setFilters(filters);
        LogUtils.setExclusions(Lists.newArrayList("update","consumableEvents", "childrenConsumableEvents"));

        final Collection<Events> allConsumableEvents = TreeUtils.getAllConsumableEventsBelow(appRootNode);
        randomColorButton.it.dispatchEvent(new MouseEvent(randomColorButton.it, 1, new Date().getTime(), InputEvent.BUTTON1_DOWN_MASK, 1,1,1, false, 1));
    }
}
