/*
 * JDigitalSimulator
 * Copyright (C) 2017 Kristian Kraljic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package lc.kra.jds.components.buildin.flipflop;

import static lc.kra.jds.Utilities.*;

import java.awt.Graphics;
import java.awt.Point;

import lc.kra.jds.Utilities.TranslationType;
import lc.kra.jds.components.buildin.flipflop.FlipFlop.Clocked;
import lc.kra.jds.components.buildin.flipflop.FlipFlop.MasterSlave;
import lc.kra.jds.contacts.Contact;
import lc.kra.jds.contacts.ContactUtilities;
import lc.kra.jds.contacts.InputContact;
import lc.kra.jds.contacts.OutputContact;

/**
 * D-Flip-Flop (build-in component)
 * @author Kristian Kraljic (kris@kra.lc)
 */
@Clocked @MasterSlave
public class DFlipFlop extends FlipFlop {
	private static final long serialVersionUID = 2l;

	private static final String KEY;
	static { KEY = "component.flipflop."+DFlipFlop.class.getSimpleName().toLowerCase(); }
	public static final ComponentAttributes componentAttributes = new ComponentAttributes(KEY, getTranslation(KEY), "group.flipflop", getTranslation(KEY, TranslationType.DESCRIPTION), "Kristian Kraljic (kris@kra.lc)", 1);

	private InputContact inputD, inputC;
	private OutputContact outputQ, outputQi;
	private Contact[] contacts;

	transient private boolean oldD, oldClock;

	public DFlipFlop() {
		int top = size.height / 4;
		inputD = new InputContact(this, new Point(0, top));
		inputC = new InputContact(this, new Point(0, top*2));
		top = size.height / 3;
		outputQ = new OutputContact(this, new Point(size.width, top));
		outputQi = new OutputContact(this, new Point(size.width, top*2));
		outputQi.setCharged();
		contacts = new Contact[]{inputD, inputC, outputQ, outputQi};
	}

	@Override public void paint(Graphics graphics) {
		super.paint(graphics);
		paintDefaultContacts(graphics, "D");
		ContactUtilities.paintSolderingJoints(graphics, 10, 10, inputD, inputC, outputQ);
		ContactUtilities.paintSolderingJoints(graphics, 3, 3, outputQi);
	}

	@Override public Contact[] getContacts() { return contacts; }
	@Override public void calculate() {
		if(isMasterSlave) {
			if(inputC.isCharged()&&!oldClock) //positive edge
				oldD = inputD.isCharged();
		} else oldD = inputD.isCharged();
		if(isTiggered(inputC.isCharged(), oldClock)) {
			outputQ.setCharged(oldD); //set
			outputQi.setCharged(!outputQ.isCharged());
		}
		oldClock = inputC.isCharged();
	}
}
