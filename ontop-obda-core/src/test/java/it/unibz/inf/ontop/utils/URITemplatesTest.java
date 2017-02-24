package it.unibz.inf.ontop.utils;

/*
 * #%L
 * ontop-obdalib-core
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Arrays;

import it.unibz.inf.ontop.model.Function;
import it.unibz.inf.ontop.model.OBDADataFactory;
import it.unibz.inf.ontop.model.impl.OBDADataFactoryImpl;

import org.junit.Test;

import static it.unibz.inf.ontop.model.impl.OntopModelSingletons.DATA_FACTORY;
import static org.junit.Assert.assertEquals;

public class URITemplatesTest {
	
	@SuppressWarnings("unchecked")
    @Test
	public void testFormat(){
		assertEquals("http://example.org/A/1", URITemplates.format("http://example.org/{}/{}", "A", 1));
		
		assertEquals("http://example.org/A", URITemplates.format("http://example.org/{}", "A"));
		
		assertEquals("http://example.org/A/1", URITemplates.format("http://example.org/{}/{}", Arrays.asList("A", 1)));
		
		assertEquals("http://example.org/A", URITemplates.format("http://example.org/{}", Arrays.asList("A")));

        assertEquals("http://example.org/A", URITemplates.format("{}", Arrays.asList("http://example.org/A")));
	}

    @Test
    public void testGetUriTemplateString1(){
        Function f1 = DATA_FACTORY.getUriTemplate(DATA_FACTORY.getConstantLiteral("http://example.org/{}/{}"), //
                DATA_FACTORY.getVariable("X"), DATA_FACTORY.getVariable("Y"));
        assertEquals("http://example.org/{X}/{Y}", URITemplates.getUriTemplateString(f1));
    }

    @Test
    public void testGetUriTemplateString2(){
        Function f1 = DATA_FACTORY.getUriTemplate(DATA_FACTORY.getConstantLiteral("{}"), //
                DATA_FACTORY.getVariable("X"));
        assertEquals("{X}", URITemplates.getUriTemplateString(f1));
    }

    @Test
    public void testGetUriTemplateString3(){

        Function f1 = DATA_FACTORY.getUriTemplate(DATA_FACTORY.getConstantLiteral("{}/"), //
                DATA_FACTORY.getVariable("X"));
        assertEquals("{X}/", URITemplates.getUriTemplateString(f1));
    }

    @Test
    public void testGetUriTemplateString4(){

        Function f1 = DATA_FACTORY.getUriTemplate(DATA_FACTORY.getConstantLiteral("http://example.org/{}/{}/"), //
                DATA_FACTORY.getVariable("X"), DATA_FACTORY.getVariable("Y"));
        assertEquals("http://example.org/{X}/{Y}/", URITemplates.getUriTemplateString(f1));
    }

    @Test
    public void testGetUriTemplateString5(){

        Function f1 = DATA_FACTORY.getUriTemplate(DATA_FACTORY.getConstantLiteral("http://example.org/{}/{}/{}"), //
                DATA_FACTORY.getVariable("X"), DATA_FACTORY.getVariable("Y"),DATA_FACTORY.getVariable("X"));
        assertEquals("http://example.org/{X}/{Y}/{X}", URITemplates.getUriTemplateString(f1));
    }
}