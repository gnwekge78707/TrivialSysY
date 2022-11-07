package backend.template;

import backend.MipsAssembly;
import backend.isa.MipsInstruction;

public class MipsOtherTemplate {
    public static void mipsProcessTag(String tagString, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getTag(tagString));
    }

    public static void mipsProcessComment(String comment, MipsAssembly assembly) {
        assembly.addObjectCode(MipsInstruction.getComment(comment));
    }

    public static void processAllocMainStack(int space, MipsAssembly assembly) {
        MipsCalTemplate.mipsAddNumTemplate(MipsAssembly.sp, MipsAssembly.sp, -space, assembly);
    }




}
