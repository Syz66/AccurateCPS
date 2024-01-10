package me.zircta.accuratecps.hook;

import net.weavemc.loader.api.Hook;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class CPSHook extends Hook {
    @Override
    public void transform(@NotNull ClassNode classNode, @NotNull AssemblerConfig assemblerConfig) {
        int listCount = 0, methodCount = 0;
        boolean leftClick = false, rightClick = false;

        if (classNode.name.startsWith("com/moonsworth/lunar/client")) {
            for (FieldNode field : classNode.fields) {
                if (field.desc.equals("Lit/unimi/dsi/fastutil/longs/LongList;")) {
                    listCount++;
                }
            }

            for (MethodNode method : classNode.methods) {
                if (listCount == 2) {
                    for (AbstractInsnNode insn : method.instructions) {
                        int opcode = insn.getOpcode();

                        if (opcode == Opcodes.IFNE) {
                            leftClick = true;
                        }

                        if (insn.getNext() != null) {
                            int nextOpcode = insn.getNext().getOpcode();

                            if (opcode == Opcodes.ICONST_1 && nextOpcode == Opcodes.IF_ICMPNE) {
                                rightClick = true;
                            }
                        }
                    }

                    if (leftClick && rightClick) {
                        if (method.desc.equals("()I") && methodCount < 2) {
                            method.instructions.clear();
                            method.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "me/zircta/accuratecps/utils/CPSHandler", "INSTANCE", "Lme/zircta/accuratecps/utils/CPSHandler;"));
                            method.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "me/zircta/accuratecps/utils/CPSHandler", methodCount < 1 ? "getLeftCps" : "getRightCps", "()I"));
                            method.instructions.add(new InsnNode(Opcodes.IRETURN));
                            methodCount++;
                        }
                    }
                }
            }
        }
    }
}