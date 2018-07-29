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

import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.StatechartFluentBuilder;
import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.StatechartIntegrationTest;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchitekturTest {

    protected final JavaClasses klassen = new ClassFileImporter().withImportOption(ImportOption.Predefined.DONT_INCLUDE_TESTS)
        .importPackages("com.github.funthomas424242.rades.fluentbuilder.statechart");


    @Test
    public void noAccessGeneratedToGenerators() {

        final ArchRule rule = noClasses()
            .that().resideOutsideOfPackage("..generators..")
            .should().accessClassesThat().resideInAPackage("..generators..");

        rule.check(klassen);
    }


    @Test
    public void accessOfDomainObjects() {

        classes()
            .that().resideInAPackage("..domain")
            .should().accessClassesThat().resideInAnyPackage("..generated.."
            , "..domain", "..rades.annotations..", "java..")
            .check(klassen);
    }


    @Test
    public void noAccessTransitionToGenerators() {

        noClasses().that().haveNameMatching(".*statechart.Transition")
            .should().accessClassesThat().resideInAPackage("..generators..")
            .check(klassen);
    }


    @Test
    public void noCyles() {

        slices().matching("com.github.funthomas424242.rades.fluentbuilder.statechart.(*)..")
            .should().beFreeOfCycles()
            .check(klassen);
    }

    @Test
    public void accessOfDomainPackageOnlyFromStatechartSubs() {

        final ArchRule myRule = classes()
            .that().resideInAPackage("..statechart")
            .should().onlyBeAccessed().byAnyPackage("..statechart..");

        myRule.check(klassen);
    }

    @Test
    public void noAccessFromItemsToStatechart() {

        classes().that().haveSimpleName("AbstractStatechartFluentBuilder")
            .should().onlyBeAccessed().byClassesThat().haveNameMatching(".*(" + StatechartIntegrationTest.class.getSimpleName() + "|" + StatechartFluentBuilder.class.getSimpleName() + ")").check(klassen);

        noClasses().that().haveSimpleName("State")
            .should().accessClassesThat().haveNameMatching(".*Statechart.*").check(klassen);

        noClasses().that().haveSimpleName("Transiton")
            .should().accessClassesThat().haveNameMatching(".*Statechart.*").check(klassen);

    }


}


