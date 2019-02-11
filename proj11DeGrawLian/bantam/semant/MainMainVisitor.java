package proj11DeGrawLian.bantam.semant;

import proj11DeGrawLian.bantam.ast.*;
import proj11DeGrawLian.bantam.visitor.Visitor;

public class MainMainVisitor extends Visitor {
    private boolean hasMain = false;

    public boolean hasMain(Program ast){
        ast.accept(this);
        return hasMain;
    }

    /**
     * Visit a list node of classes
     *
     * @param node the class list node
     * @return result of the visit
     */
    public Object visit(ClassList node) {
        for (ASTNode aNode : node){
            if(!hasMain)
                aNode.accept(this);
        }
        return null;
    }

    /**
     *
     * @param node the Class_ node being visited
     * @return result of the visit
     */
    public Object visit(Class_ node) {
        super.visit(node);

        // get class name
        String curClassName = node.getName();

        if(!curClassName.equals("Main")){
            hasMain = false;
        }

        return null;
    }


    /**
     *
     * @param node the Method node being visited
     * @return result of the visit
     */
    public Object visit(Method node) {
//        super.visit(node); //TODO: does it still need to be visited?

        // get method name
        String methodName = node.getName();

        // get return type
        String returnType = node.getReturnType();

        // get num params for this method
        int numParams = node.getFormalList().getSize();

        if(methodName.equals("main") && returnType.equals("void") && numParams == 0){
            hasMain = true;
        }

        return null;
    }
}