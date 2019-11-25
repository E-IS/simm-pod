/*
 * #%L
 * SUMARiS
 * %%
 * Copyright (C) 2019 SUMARiS Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package net.sumaris.server.http.rest;

import com.google.common.base.Splitter;
import net.sumaris.core.exception.SumarisTechnicalException;
import net.sumaris.core.util.StringUtils;
import net.sumaris.rdf.config.RdfConfiguration;
import net.sumaris.rdf.service.RdfExportOptions;
import net.sumaris.rdf.service.RdfExportService;
import net.sumaris.rdf.util.ModelUtils;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@RestController
@ConditionalOnBean({RdfConfiguration.class})
public class RdfRestController {

    private static final Logger log = LoggerFactory.getLogger(RdfRestController.class);

    @Resource(name = "rdfModelExportService")
    private RdfExportService service;

    @PostConstruct
    public void afterPropertySet() {
        log.info(String.format("Starting RDF rest controller at {%s}...", RdfRestPaths.RDF_BASE_PATH));
    }

    @GetMapping(value = RdfRestPaths.ONTOLOGY_PATH, produces = { MediaType.APPLICATION_XML_VALUE, RdfMediaType.APPLICATION_RDF_XML_VALUE })
    @ResponseBody
    public String getOwl(@PathVariable  String name,
                         @RequestParam(defaultValue = "rdf") String format,
                         @RequestParam(defaultValue = "false") String disjoints,
                         @RequestParam(defaultValue = "false") String methods,
                         @RequestParam(required = false) String packages) {

        RdfExportOptions options = buildOptions(disjoints, methods, packages);
        Model model = service.getOntModelWithClasses(name, options);
        return ModelUtils.modelToString(model, format);
    }

    @GetMapping(value = RdfRestPaths.ONTOLOGY_CLASS_PATH,
            produces = { MediaType.APPLICATION_XML_VALUE, RdfMediaType.APPLICATION_RDF_XML_VALUE })
    @ResponseBody
    public String getOwlClass(@PathVariable String name,
                              @PathVariable(name = "class") String className,
                              @RequestParam(defaultValue = "rdf") String format,
                              @RequestParam(defaultValue = "false") String disjoints,
                              @RequestParam(defaultValue = "false") String methods,
                              @RequestParam(required = false) String packages) {

        RdfExportOptions options = buildOptions(disjoints, methods, packages);
        options.setClassname(className);
        Model model = service.getOntModelWithClasses(name, options);
        return ModelUtils.modelToString(model, format);
    }

    @GetMapping(value = RdfRestPaths.DATA_CLASS_PATH,
            produces = { MediaType.APPLICATION_XML_VALUE, RdfMediaType.APPLICATION_RDF_XML_VALUE })
    @ResponseBody
    public String getOwlWithInstance(@PathVariable String name,
                                     @PathVariable(name = "class")  String className,
                                     @RequestParam(defaultValue = "rdf") String format,
                                     @RequestParam(defaultValue = "false") String disjoints,
                                     @RequestParam(defaultValue = "false") String methods,
                                     @RequestParam(required = false) String packages) {
        RdfExportOptions options = buildOptions(disjoints, methods, packages);
        options.setClassname(className);
        Model model = service.getOntModelWithInstances(name, options);
        return ModelUtils.modelToString(model, format);
    }


    @GetMapping(value = RdfRestPaths.RDF_TYPE_NAME_PATH, produces = { MediaType.APPLICATION_XML_VALUE, RdfMediaType.APPLICATION_RDF_XML_VALUE })
    @ResponseBody
    public String getModelByTypeAndName(@PathVariable String format,
                                        @PathVariable String type,
                                        @PathVariable String name,
                                        @RequestParam(defaultValue = "false") String disjoints,
                                        @RequestParam(defaultValue = "false") String methods,
                                        @RequestParam(required = false) String packages) {

        RdfExportOptions options = buildOptions(disjoints, methods, packages);
        Model model;
        switch (type) {
            case "ontologies":
            case "ontology":
                model = service.getOntModelWithClasses(name, options);
                break;
            case "data":
                model = service.getOntModelWithInstances(name, options);
                break;
            default:
                throw new SumarisTechnicalException(String.format("Unknown model type {%s}", type));
        }

        return ModelUtils.modelToString(model, format);
    }

    @GetMapping(value = RdfRestPaths.RDF_TYPE_NAME_CLASS_PATH, produces = { MediaType.APPLICATION_XML_VALUE, RdfMediaType.APPLICATION_RDF_XML_VALUE })
    @ResponseBody
    public String getModelByTypeAndNameAndClass(@PathVariable String format,
                                                @PathVariable String type,
                                                @PathVariable String name,
                                                @PathVariable(name = "class")  String className,
                                                @RequestParam(defaultValue = "false") String disjoints,
                                                @RequestParam(defaultValue = "false") String methods,
                                                @RequestParam(required = false) String packages) {

        RdfExportOptions options = buildOptions(disjoints, methods, packages);
        options.setClassname(className);

        Model model;
        switch (type) {
            case "ontologies":
            case "ontology":
                model = service.getOntModelWithClasses(name, options);
                break;
            case "data":
                model = service.getOntModelWithInstances(name, options);
                break;
            default:
                throw new SumarisTechnicalException(String.format("Unknown model type {%s}", type));
        }

        return ModelUtils.modelToString(model, format);
    }
        /*
     * ************* Service methods *************
     */

    @GetMapping(value = RdfRestPaths.RDF_NTRIPLE_PATH, produces = {RdfMediaType.APPLICATION_N_TRIPLES_VALUE, RdfMediaType.TEXT_N_TRIPLES_VALUE})
    @ResponseBody
    public String getModelAsNTriples(@PathVariable(name = "type") String type,
                                     @PathVariable(name = "name")  String name,
                                     @RequestParam(defaultValue = "false") String disjoints,
                                     @RequestParam(defaultValue = "false") String methods,
                                     @RequestParam(required = false) String packages) {

        return getModelByTypeAndName("ntriple", type, name, disjoints, methods, packages);
    }
//
//    @GetMapping(value = "/ttl/{q}/{name}", produces = {"text/turtle"})
//    @ResponseBody
//    public String getModelAsTurtle(@PathVariable String q, @PathVariable String name,
//                                   @RequestParam(defaultValue = "false") String disjoints,
//                                   @RequestParam(defaultValue = "false") String methods,
//                                   @RequestParam(required = false) String packages) {
//        return ModelUtils.modelToString(generateModel(q, name, disjoints, methods, packages), "TURTLE");
//    }
//
//    @GetMapping(value = "/n3/{q}/{name}", produces = {"text/n3", "application/text", "text/text"})
//    @ResponseBody
//    public String getModelAsN3(@PathVariable String q, @PathVariable String name,
//                               @RequestParam(defaultValue = "false") String disjoints,
//                               @RequestParam(defaultValue = "false") String methods,
//                               @RequestParam(required = false) String packages) {
//        return ModelUtils.modelToString(generateModel(q, name, disjoints, methods, packages), "N3");
//    }
//
//    @GetMapping(value = "/json/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
//    @ResponseBody
//    public String getModelAsrdfjson(@PathVariable String q, @PathVariable String name,
//                                    @RequestParam(defaultValue = "false") String disjoints,
//                                    @RequestParam(defaultValue = "false") String methods,
//                                    @RequestParam(required = false) String packages) {
//        return ModelUtils.modelToString(generateModel(q, name, disjoints, methods, packages), "RDF/JSON");
//    }
//
//    @GetMapping(value = "/trig/{q}/{name}", produces = {"text/trig"})
//    @ResponseBody
//    public String getModelAsTrig(@PathVariable String q, @PathVariable String name,
//                                 @RequestParam(defaultValue = "false") String disjoints,
//                                 @RequestParam(defaultValue = "false") String methods,
//                                 @RequestParam(required = false) String packages) {
//        return ModelUtils.modelToString(generateModel(q, name, disjoints, methods, packages), "TriG");
//    }
//
//    @GetMapping(value = "/jsonld/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
//    @ResponseBody
//    public String getModelAsJson(@PathVariable String q, @PathVariable String name,
//                                 @RequestParam(defaultValue = "false") String disjoints,
//                                 @RequestParam(defaultValue = "false") String methods,
//                                 @RequestParam(required = false) String packages) {
//        return ModelUtils.modelToString(generateModel(q, name, disjoints, methods, packages), "JSON-LD");
//    }
//
//    @GetMapping(value = "/rdf/{q}/{name}", produces = {MediaType.APPLICATION_XML_VALUE, RdfMediaType.APPLICATION_RDF_XML_VALUE})
//    public String getModelAsXml(@PathVariable String q, @PathVariable String name,
//                                @RequestParam(defaultValue = "false") String disjoints,
//                                @RequestParam(defaultValue = "false") String methods,
//                                @RequestParam(required = false) String packages) {
//
//        Model model = generateModel(q, name, disjoints, methods, packages);
//        return ModelUtils.modelToString(model, "RDF/XML");
//    }
//
//    @GetMapping(value = "/trix/{q}/{name}", produces = {"text/trix"})
//    @ResponseBody
//    public String getModelAsTrix(@PathVariable String q, @PathVariable String name,
//                                 @RequestParam(defaultValue = "false") String disjoints,
//                                 @RequestParam(defaultValue = "false") String methods,
//                                 @RequestParam(required = false) String packages) {
//        fillObjectWithStdAttribute(null,null,null);
//        return ModelUtils.modelToString(generateModel(q, name, disjoints, methods, packages), "TriX");
//    }
//
//    @GetMapping(value = "/vowl/{q}/{name}", produces = {"application/x-javascript", "application/json", "application/ld+json"})
//    @ResponseBody
//    public String getModelAsVowl(@PathVariable(name = "q") String query,
//                                 @PathVariable String name,
//                                 @RequestParam(defaultValue = "false") String disjoints,
//                                 @RequestParam(defaultValue = "false") String methods,
//                                 @RequestParam(required = false) String packages) {
//        Model res =  generateModel(query, name, disjoints, methods, packages);
//        return ModelUtils.modelToString(res, "VOWL");
//    }

    /* -- protected methods -- */


    protected RdfExportOptions buildOptions(String disjoints, String methods, String packages) {

        return RdfExportOptions.builder()
                .withDisjoints("true".equalsIgnoreCase(disjoints))
                .withMethods("true".equalsIgnoreCase(methods))
                .packages(StringUtils.isNotBlank(packages) ? Splitter.on(',').omitEmptyStrings().splitToList(packages) : null)
                .build();
    }

}