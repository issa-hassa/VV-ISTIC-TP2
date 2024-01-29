package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MembersWithoutGetter extends VoidVisitorWithDefaults<Void> {
    private File result;
    private StringBuilder htmlContent;

    public MembersWithoutGetter() {
        result = new File("result.html");
        htmlContent = new StringBuilder("<html><body><ul>");
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                type.accept(this, null);
            }
        }
        htmlContent.append("</ul></body></html>");
        writeHtmlToFile(htmlContent.toString());
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        String path = declaration.getFullyQualifiedName().orElse("[Anonymous]");



        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member.isFieldDeclaration() && !((FieldDeclaration) member).isPublic()) {
                String fieldName = ((FieldDeclaration) member).getVariable(0).getName().toString();
                boolean found = false;

                for (MethodDeclaration method : declaration.getMethods()) {
                    if (!method.isPublic()) continue;

                    String methodName = method.getName().toString();

                    if (methodName.equalsIgnoreCase("get" + fieldName) && doesMethodReturnField(method, fieldName)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    htmlContent.append("<li>").append("L'attribut ").append(fieldName)
                            .append(" n'a pas de getter public dans ").append(path).append("</li>");
                }
            }
        }


        // Write the HTML content to the result file

    }

    private boolean doesMethodReturnField(MethodDeclaration method, String fieldName) {
        if (!method.getBody().isPresent()) {
            return false;
        }

        NodeList<Statement> statements = method.getBody().get().getStatements();
        int bodySize = statements.size();

        // Check if the method contains a single return statement with the field
        return bodySize == 1 &&
                statements.get(0) instanceof ReturnStmt &&
                ((ReturnStmt) statements.get(0)).getExpression().map(
                        expr -> expr.toString().equals(fieldName) || expr.toString().equals("this." + fieldName)
                ).orElse(false);
    }

    private void writeHtmlToFile(String htmlContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(result))) {
            writer.write(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }
}
