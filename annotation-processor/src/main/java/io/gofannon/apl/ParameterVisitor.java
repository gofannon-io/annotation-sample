package io.gofannon.apl;

import javax.lang.model.element.*;

public class ParameterVisitor implements ElementVisitor<StringVisitResult, ElementContext> {

    @Override
    public StringVisitResult visit(Element e, ElementContext elementContext) {
       return null;
    }

    @Override
    public StringVisitResult visitPackage(PackageElement e, ElementContext elementContext) {
        return null;
    }

    @Override
    public StringVisitResult visitType(TypeElement e, ElementContext elementContext) {
        return null;
    }

    @Override
    public StringVisitResult visitVariable(VariableElement e, ElementContext elementContext) {
        //((Symbol.VarSymbol) e).getConstValue()
        Object constValue = e.getConstantValue();
        System.out.println("ConstantValue "+constValue);
        StringVisitResult result = new StringVisitResult();
        result.setFieldValue(constValue==null? null : constValue.toString());
        return result;
    }

    @Override
    public StringVisitResult visitExecutable(ExecutableElement e, ElementContext elementContext) {
        return null;
    }

    @Override
    public StringVisitResult visitTypeParameter(TypeParameterElement e, ElementContext elementContext) {
        return null;
    }

    @Override
    public StringVisitResult visitUnknown(Element e, ElementContext elementContext) {
        return null;
    }

    @Override
    public StringVisitResult visit(Element e) {
        return ElementVisitor.super.visit(e);
    }

    @Override
    public StringVisitResult visitModule(ModuleElement e, ElementContext elementContext) {
        return ElementVisitor.super.visitModule(e, elementContext);
    }

    @Override
    public StringVisitResult visitRecordComponent(RecordComponentElement e, ElementContext elementContext) {
        return ElementVisitor.super.visitRecordComponent(e, elementContext);
    }
}