package org.fastcode.preferences;

import static org.fastcode.common.FastCodeConstants.COMMA;
import static org.fastcode.common.FastCodeConstants.DOT;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTSetDirective;
import org.apache.velocity.runtime.parser.node.ASTStringLiteral;
import org.apache.velocity.runtime.visitor.BaseVisitor;
import org.fastcode.common.FastCodeAdditionalParams;
import org.fastcode.util.TemplateDetailsForVelocity;
import org.fastcode.util.VelocityUtil;

public class TemplateVisitor extends BaseVisitor {
	final List<String>	varList				= new ArrayList<String>();
	final List<String>	localVars			= new ArrayList<String>();
	final List<String>	setVars				= new ArrayList<String>();
	boolean				isSetDirective		= false;
	String				invalidVariable		= EMPTY_STR;
	boolean				isForDirective		= false;
	boolean				isMacrodirective	= false;
	String				forLoopLocalVar		= EMPTY_STR;
	final List<String>	tmpvarList			= new ArrayList<String>();

	@Override
	public Object visit(final ASTReference node, final Object data) {
		final String name = node.literal();
		final String varName = VelocityUtil.getInstance().getVariableName(name);
		if (this.isSetDirective) {

			if (!this.setVars.contains(varName)) {
				this.setVars.add(varName);
			}

		}
		boolean isValidVar = false;
		if (!this.tmpvarList.contains(varName)) {
			if (((TemplateDetailsForVelocity) data).isDoValidation()) {
				isValidVar = new VelocityUtil().validateVariables(varName, this.isForDirective, this.setVars, this.isMacrodirective,
						this.forLoopLocalVar, data);
			} else {
				isValidVar = true;
			}
		} else {
			isValidVar = true;
		}

		if (isValidVar) {
			if (!this.varList.contains(name) && !this.isSetDirective) {
				this.varList.add(name);
			}
			if (!this.tmpvarList.contains(varName)) {
				this.tmpvarList.add(varName);
			}
		} else {
			if (!this.invalidVariable.contains(varName)) {
				this.invalidVariable = this.invalidVariable + COMMA + varName;
			}

		}
		this.isSetDirective = false;
		return super.visit(node, data);
	}

	public List<String> getVarList() {
		return this.varList;
	}

	@Override
	public Object visit(final ASTDirective node, final Object data) {
		if (node.getDirectiveName().equalsIgnoreCase("foreach")) {
			this.isForDirective = true;
			this.forLoopLocalVar = node.jjtGetChild(0).literal().substring(1);
			this.localVars.add(node.jjtGetChild(0).literal());
			this.localVars.add(node.jjtGetChild(2).literal());

		}
		if (node.getDirectiveName().equalsIgnoreCase("macro")) {
			this.isMacrodirective = true;
		} else {
			for (final FastCodeAdditionalParams additionalParams : ((TemplateDetailsForVelocity) data).getAdditionalPram()) {
				if (node.getDirectiveName().equalsIgnoreCase(additionalParams.getName())) {
					this.varList.add(node.getDirectiveName());
					break;
				}
			}

		}
		node.childrenAccept(this, data);
		this.isForDirective = false;
		this.isMacrodirective = false;
		return data;

	}

	@Override
	public Object visit(final ASTSetDirective node, final Object data) {
		this.isSetDirective = true;

		return super.visit(node, data);
	}

	public List<String> getLocalVars() {
		return this.localVars;
	}

	@Override
	public Object visit(final ASTStringLiteral node, final Object data) {
		String name = node.literal();
		name = name.replaceAll("\"", "");
		if (name.trim().length() > 0) {
			String varName = EMPTY_STR;
			if (name.contains("${")) {
				final String[] tempStrArr = name.split("\\$\\{");
				for (int i = 0; i < tempStrArr.length; i++) {
					if (tempStrArr[i].trim().length() == 0) {
						continue;
					}
					String tempVar = tempStrArr[i];
					if (tempStrArr[i].indexOf("}") < 0) {
						continue;
					}
					tempVar = tempStrArr[i].substring(0, tempStrArr[i].indexOf("}"));
					if (tempVar.contains(DOT)) {
						varName = tempVar.substring(0, tempVar.indexOf(DOT));
					}
					final boolean isValidVar = new VelocityUtil().validateVariables(varName, this.isForDirective, this.setVars,
							this.isMacrodirective, this.forLoopLocalVar, data);
					if (isValidVar) {
						if (!this.varList.contains(tempVar)) {
							this.varList.add("${" + tempVar + "}");
						}
					} else {
						if (!this.invalidVariable.contains(varName)) {
							this.invalidVariable = this.invalidVariable + COMMA + varName;
						}

					}
				}

			}
		}
		return data;
	}

	public String getInvalidVariable() {
		return this.invalidVariable;
	}

	public void setInvalidVariable(final String invalidVariable) {
		this.invalidVariable = invalidVariable;
	}

	public List<String> getSetVars() {
		return this.setVars;
	}

}
