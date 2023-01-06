package parser;

import grammar.GrammarItem;
import grammar.NonTerminal;
import grammar.Terminal;
import grammar.Type;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    private final GrammarItem node;
    private final List<Tree> children = new ArrayList<>();
    private Type type;

    public Map<String, String> synthesizedAttr = new HashMap<>();


    private class Edge {
        private final int from;
        private final int to;

        public Edge(final int from, final int to) {
            this.from = from;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }
    }


    public Tree(final GrammarItem node) {
        this.node = node;
    }

    public Tree(final GrammarItem node, final Type type) {
        this.node = node;
        this.type = type;
    }

    public GrammarItem getNode() {
        return node;
    }

    public void addChild(final Tree child) {
        children.add(child);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void walkthroughGraphviz(String path) {
        final List<Edge> edges = new ArrayList<>();
        final List<String> nodeConfig = new ArrayList<>();

        walkthroughGraphvizImpl(edges, nodeConfig, this);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write("strict graph TranslatorGenerator {\n");
            for (int i = 0; i != nodeConfig.size(); ++i) {
                writer.write(String.format("    %d[%s];\n", i + 1, nodeConfig.get(i)));
            }
            for (Edge edge : edges) {
                writer.write(String.format("    %d -- %d;\n", edge.getFrom() + 1, edge.getTo() + 1));
            }
            writer.write("}\n");
        } catch (IOException e) {
            System.err.printf("Error occurred while writing result into file '%s'", path);
        }
    }


    private void walkthroughGraphvizImpl(List<Edge> edges, List<String> nodeConfig, Tree tree) {
        int treeID = nodeConfig.size();
        nodeConfig.add("");

        StringBuilder stringBuilder = new StringBuilder();
        if (tree.children.isEmpty()) {
            String value = ((Terminal) tree.node).getParsedValue();
            stringBuilder
                    .append("label=\"")
                    .append(value.isEmpty() ? "[EPS]" : value)
                    .append("\" shape=box color=green");
            nodeConfig.set(treeID, stringBuilder.toString());
            return;
        }

        stringBuilder
                .append("label=\"")
                .append(((NonTerminal) tree.node).getValue())
                .append("\" shape=circle");
        nodeConfig.set(treeID, stringBuilder.toString());

        for (Tree child : tree.children) {
            int childID = nodeConfig.size();
            edges.add(new Edge(treeID, childID));
            walkthroughGraphvizImpl(edges, nodeConfig, child);
        }
    }
}
