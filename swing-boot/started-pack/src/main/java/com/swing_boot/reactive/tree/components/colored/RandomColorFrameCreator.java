package com.swing_boot.reactive.tree.components.colored;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.swing_boot.reactive.tree.nodes.JButtonNode;
import com.swing_boot.reactive.tree.nodes.JPanelNode;
import com.swing_boot.reactive.tree.nodes.JScrollPaneNode;

import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Insets;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class RandomColorFrameCreator {

    public static RandomColorFrame createTestableFrame(final int treeDepth) {
        final JPanelNode mainPanelNode = new RandomColorPanel();
        mainPanelNode.setLayout(new GridLayoutManager(1,1));

        createJPanelsBranch(mainPanelNode, treeDepth);

        final RandomColorFrame randomColorFrame = new RandomColorFrame();

        final JScrollPaneNode<JScrollPane> jScrollPaneNode = new RandomColorScrollPane(){};
        jScrollPaneNode.it.setViewportView(mainPanelNode.it);
        jScrollPaneNode.it.getVerticalScrollBar().setUnitIncrement(20);
        jScrollPaneNode.it.getHorizontalScrollBar().setUnitIncrement(20);

        jScrollPaneNode.addChild(mainPanelNode);
        randomColorFrame.addRoot(jScrollPaneNode);
        return randomColorFrame;
    }

    private static JPanelNode createJPanelsBranch(final JPanelNode jPanelNode, int depth) {

        final JPanelNode panel1 = new RandomColorPanel();
        final JPanelNode panel2 = new RandomColorPanel();
        final JPanelNode panel3 = new RandomColorPanel();

        final JButtonNode colorButton2  = new RandomColorButton();
        colorButton2.it.setPreferredSize(new Dimension(45, 45));

        jPanelNode.setLayout(new GridLayoutManager(2,2, new Insets(0, 0, 0, 0), 0, 0));
        jPanelNode.add(colorButton2, new GridConstraints(0, 0, 1, 1, ANCHOR_CENTER, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        jPanelNode.add(panel1, new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        jPanelNode.add(panel2, new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        jPanelNode.add(panel3, new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        if (depth != 0) {
            createJPanelsBranch(panel1, depth - 1);
            createJPanelsBranch(panel2, depth - 1);
            createJPanelsBranch(panel3, depth - 1);
        }
        return jPanelNode;
    }
}
