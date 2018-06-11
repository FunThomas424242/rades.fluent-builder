package com.github.funthomas424242.rades.fluentbuilder.statechart;

/*-
 * #%L
 * rades.fluent-builder
 * %%
 * Copyright (C) 2018 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitekturTest {

    protected final JavaClasses klassen = new ClassFileImporter().importPackages("com.github.funthomas424242.rades.fluentbuilder.statechart");

//
//    @Test
//    public void accessOfDomainPackage() {
//
//
//        final ArchRule myRule = klassen()
//            .that().resideInAPackage("..statechart")
//            .should().onlyBeAccessed().byAnyPackage("..statechart..");
//
//        myRule.check(klassen);
//    }

    @Test
    public void noAccessFromStateToStatechart() {
//        classes().that().haveSimpleName("GeneratedAbstractStatechart")
//            .should().onlyBeAccessed().byClassesThat().haveSimpleNameStartingWith("Statechart")
//            .check(klassen);

        classes().that().haveSimpleName("GeneratedAbstractStatechart")
            .should().onlyBeAccessed().byClassesThat().haveNameMatching(".*(StatechartTest|StatechartBuilder)").check(klassen);

        noClasses().that().haveSimpleName("State")
            .should().accessClassesThat().haveNameMatching(".*Statechart.*").check(klassen);

        noClasses().that().haveSimpleName("Transiton")
            .should().accessClassesThat().haveNameMatching(".*Statechart.*").check(klassen);


//        noClasses().that().haveNameMatching(".*Statechart")
//            .should().onlyBeAccessed().byClassesThat().haveSimpleName("Transition").check(klassen);
//        noClasses().that().haveNameMatching(".*Statechart")
//            .should().onlyBeAccessed().byClassesThat().haveSimpleName("ParameterSignatur").check(klassen);

    }


}


