#include <fstream>
#include "Tree.h"


Tree::Tree(Tree::Terminal terminal)
    : node(terminal)
{}

Tree::Tree(NonTerminal nonTerminal)
    : node(nonTerminal)
{}

Tree::~Tree() {
    clear();
}


void Tree::addChild(Tree* child) {
    children.push_back(child);
}

void Tree::clear() {
    for (auto ptr : children) {
        delete ptr;
    }
}

std::vector<Tree::Terminal> Tree::walkthrough() {
    std::vector<Terminal> result;
    walkthroughImpl(result, this);
    return result;
}

void Tree::walkthroughImpl(std::vector<Terminal>& result, Tree* tree) {
    if (tree->children.empty()) {
        result.push_back(std::get<Terminal>(tree->node));
        return;
    }
    for (auto child : tree->children) {
        walkthroughImpl(result, child);
    }
}

void Tree::walkthroughGraphviz(std::string path) {
    std::vector<std::pair<size_t, size_t>> edges;
    std::vector<std::string> nodeConfig;

    walkthroughGraphvizImpl(edges, nodeConfig, this);

    std::ofstream out(path);
    out << "strict graph KotlinArray {\n";
    for (std::size_t i = 0; i != nodeConfig.size(); ++i) {
        out << "    " << i + 1 << "[" << nodeConfig[i] << "];\n";
    }
    for (auto [lhs, rhs] : edges) {
        out << "    " << lhs + 1 << " -- " << rhs + 1 << ";\n";
    }
    out << "}\n";
    out.close();
}

void Tree::walkthroughGraphvizImpl(std::vector<std::pair<size_t, size_t>>& edges,
                                   std::vector<std::string>& nodeConfig,
                                   Tree* tree) {
    std::size_t treeID = nodeConfig.size();
    nodeConfig.emplace_back();

    if (tree->children.empty()) {
        nodeConfig[treeID] += "label=\"";
        nodeConfig[treeID] += Token2String[std::get<Terminal>(tree->node)];
        nodeConfig[treeID] += "\" shape=box color=green";
        return;
    } else {
        nodeConfig[treeID] += "label=\"";
        nodeConfig[treeID] += NonTerminal2String[std::get<NonTerminal>(tree->node)];
        nodeConfig[treeID] += "\" shape=circle";
    }

    for (auto child : tree->children) {
        std::size_t childID = nodeConfig.size();
        edges.emplace_back(treeID, childID);
        walkthroughGraphvizImpl(edges, nodeConfig, child);
    }
}

