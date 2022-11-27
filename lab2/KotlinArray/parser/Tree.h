#pragma once

#include "../helpers/FirstFollow.h"


class Tree {
public:
    using Terminal = FirstFollow::Terminal;

    Tree() = delete;
    explicit Tree(Terminal);
    explicit Tree(NonTerminal);
    ~Tree();

    void addChild(Tree*);
    void clear();
    std::vector<Terminal> walkthrough();
    void walkthroughGraphviz(std::string);

private:
    static void walkthroughImpl(std::vector<Terminal>&, Tree*);
    static void walkthroughGraphvizImpl(std::vector<std::pair<size_t, size_t>> &edges,
                                        std::vector<std::string> &nodeConfig,
                                        Tree *tree);

private:
    std::variant<Terminal, NonTerminal> node;
    std::vector<Tree*> children;

};

