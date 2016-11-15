package com.sky.voidchat;

import java.util.Iterator;
import java.util.Map;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.IClassTransformer;

public class EDClassTransformer implements IClassTransformer{

	private String mcVersion;
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("net.minecraft.client.gui.GuiNewChat"))
		{
			return patchClassASM(transformedName, basicClass);
		}
		return basicClass;
	}
	
	public byte[] patchClassASM(String name, byte[] bytes)
	{
		mcVersion = Minecraft.getMinecraft().getVersion();
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, ClassReader.SKIP_FRAMES);

		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();
			int bipush_index = -1;
			int bipush_index2 = -1;
	
			//Check if this is doExplosionB and it's method signature is (Z)V which means that it accepts a boolean (Z) and returns a void (V)
			int i = checkFunction(m);
			if (i == 1)
			{
				
				/*
				 * FRAME CHOP 2
				    ALOAD 0
				    GETFIELD net/minecraft/client/gui/GuiNewChat.field_146253_i : Ljava/util/List;
				    INVOKEINTERFACE java/util/List.size ()I
				    BIPUSH 100 <<<<<< we want to change this value to 10.000
				    IF_ICMPLE L13
				 */
				
				AbstractInsnNode currentNode = null;
				
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				int index = -1;

				InsnList toInject = new InsnList();
				//Loop over the instruction set and find the instruction BIPUSH
				while (iter.hasNext())
				{
					index++;
					currentNode = iter.next();
	
					//Found it! save the index location of instruction FDIV and the node for this instruction
					if (currentNode.getOpcode() == Opcodes.BIPUSH)
					{
						final int new_value = 30000;
						toInject.add(new IntInsnNode(Opcodes.SIPUSH, new_value));	
						continue;
					}
					toInject.add(currentNode);
				}
				m.instructions.clear();
				m.instructions.add(toInject);
				
				
			}
			else if(i==-1)
			{
				System.out.println("[VoidChat] Voidchat does not support this forge version: " + Minecraft.getMinecraft().getVersion() + "!");
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		System.out.println("[VoidChat] Edited NewGuiChat class!");
		return writer.toByteArray();
	}
	
	public int checkFunction(MethodNode m)
	{
		/*(
			m.name.equals(targetMethodName) && m.desc.equals("(Leu;IIZ)V") && (Minecraft.getMinecraft().getVersion().contains("1.8") || Minecraft.getMinecraft().getVersion().equals("1.9"))
			)*/
		if(!m.name.equals("a"))
		{
			return 0;
		}
		if(mcVersion.startsWith("1.10"))
		{
			if(m.desc.equals("(Ley;IIZ)V"))
			{
				return 1;
			}
		}
		else if(mcVersion.startsWith("1.9.4"))
		{
			if(m.desc.equals("(Lew;IIZ)V"))
			{
				return 1;
			}
		}
		else if(mcVersion.startsWith("1.9") || mcVersion.startsWith("1.8.8") || mcVersion.startsWith("1.8.9"))
		{
			if(m.desc.equals("(Leu;IIZ)V"))
			{
				return 1;
			}
		}		
		else if(mcVersion.startsWith("1.8"))
		{
			if(m.desc.equals("(Lho;IIZ)V"))
			{
				return 1;
			}
		}		
		else if(mcVersion.startsWith("1.7.10"))
		{
			if(m.desc.equals("(Lfj;IIZ)V"))
			{
				return 1;
			}
		}
		else
		{
			return -1;
		}
		return 0;
	}

}