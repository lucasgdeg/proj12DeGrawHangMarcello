/*
 * File: NumLocalVarsVisitor.java
 * Names: Lucas DeGraw and Iris Lian
 * Class: CS 461
 * Project 11
 * Date: February 12, 2019
 */

package proj12DeGrawHangMarcello.bantam.semant;

import proj12DeGrawHangMarcello.bantam.ast.*;
import proj12DeGrawHangMarcello.bantam.visitor.Visitor;

import java.util.HashMap;
import java.util.Map;

/**
 * A subclass of the Visitor class, has the public method getNumLocalVars
 * @author Lucas DeGraw
 */
public class NumLocalVarsVisitor extends Visitor {

    // holds all mappings for the whole input program
    private HashMap<String,Integer> completeLocalVarsMap;

    // holds all mappings for one class at a time
    private HashMap<String,Integer> curClassLocalVarsMap;

    // store # of local vars for one method at a time
    private int numLocalVarsFound = 0;


    /**
     * traverses the input AST and returns the map
     *
     * @param ast an abstract syntax tree generated from Parser.parse()
     * @return a Map of ("className.MethodName",numLocalVarsInMethod) pairs
     */
    public Map<String,Integer> getNumLocalVars(Program ast) {
        completeLocalVarsMap = new HashMap<>();
        curClassLocalVarsMap = new HashMap<>();
        numLocalVarsFound = 0;

        ast.accept(this);
        return this.completeLocalVarsMap;
    }


    /**
     * called each time a Class_ node is visited
     *
     * @param node the Class_ node being visited
     * @return result of the visit
     */
    public Object visit(Class_ node) {
        super.visit(node);

        // get class name
        String curClassName = node.getName();

        // loop through key, value pairs of map for current
        this.curClassLocalVarsMap.forEach( (methodName, numLocalVars) -> {

            // add "className." prefix to each methodName key
            String newKey = curClassName + "." + methodName;

            // add the new key & corresponding # local vars to the whole map
            this.completeLocalVarsMap.put(newKey, numLocalVars);
        });

        // reset the hashmap for the current class
        this.curClassLocalVarsMap = new HashMap<>();

        return null;
    }


    /**
     * called each time a Method node is visited
     *
     * @param node the Method node being visited
     * @return result of the visit
     */
    public Object visit(Method node) {
        super.visit(node);

        // get method name
        String methodName = node.getName();

        // get num params for this method
        int numParams = node.getFormalList().getSize();

        // add methodName, numLocalVars + numParams to curClassLocalVarsMap
        this.curClassLocalVarsMap.put(methodName, this.numLocalVarsFound+numParams);

        // reset numLocalVarsFound to start counting for next method
        this.numLocalVarsFound = 0;

        return null;
    }


    /**
     * called each time a DeclStmt node is visited
     *
     * @param node the DeclExpr node being visited
     * @return result of the visit
     */
    public Object visit(DeclStmt node) {
        // increment num local vars found in the current method
        this.numLocalVarsFound++;

        return null;
    }

}