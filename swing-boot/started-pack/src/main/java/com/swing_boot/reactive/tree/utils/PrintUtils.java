package com.swing_boot.reactive.tree.utils;

import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;

import java.awt.Component;
import java.util.ArrayList;

public final class PrintUtils {

    public static void printTreeBranch(ReactiveTreeNode reactiveTreeNode, String prefix, boolean isTail, boolean geneology) {
        ArrayList<ReactiveTreeNode<? extends Component>> c = new ArrayList<>(reactiveTreeNode.getChildren());
        final String name = geneology ? reactiveTreeNode.getGenealogyName() : reactiveTreeNode.name;
        System.out.println(prefix + "│   ");
        System.out.println(prefix + (isTail ? "└──" : "├──") + name);
        for (int i = 0; i < c.size() - 1; i++) {
            printTreeBranch(c.get(i), prefix + (isTail ? "    " : "│   "), false, geneology);
        }
        if (c.size() > 0) {
            printTreeBranch(c.get(c.size() - 1), prefix + (isTail ?"    " : "│   "), true, geneology);
        }
    }
}
