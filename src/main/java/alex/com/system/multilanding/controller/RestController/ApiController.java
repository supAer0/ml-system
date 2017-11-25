package alex.com.system.multilanding.controller.RestController;

import alex.com.system.multilanding.model.*;
import alex.com.system.multilanding.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final NishaRepository nishaRepository;
    private final SiteRepository siteRepository;
    private final InstanceSiteRepository instanceSiteRepository;
    private final ElementRepository elementRepository;

    public ApiController(NishaRepository nishaRepository, SiteRepository siteRepository, InstanceSiteRepository instanceSiteRepository, ElementRepository elementRepository) {
        this.nishaRepository = nishaRepository;
        this.siteRepository=siteRepository;
        this.instanceSiteRepository = instanceSiteRepository;
        this.elementRepository = elementRepository;
    }


    @PostMapping("/createNisha")
    public ResponseEntity<Nisha> createNisha(@RequestBody Nisha input){
        Nisha nisha = new Nisha(input.getTitle());
        nishaRepository.save(nisha);
        return new ResponseEntity<Nisha>(nisha, HttpStatus.OK);
    }

    // -------------------Retrieve All ---------------------------------------------

    @RequestMapping(value = "/niches", method = RequestMethod.GET)
    public ResponseEntity<List<Nisha>> listAllNiches() {
        List<Nisha> niches = (List<Nisha>) nishaRepository.findAll();
//        if (niches.isEmpty()) {
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//            // You many decide to return HttpStatus.NOT_FOUND
//        }
        return new ResponseEntity<List<Nisha>>(niches, HttpStatus.OK);
    }

    @RequestMapping(value = "/createSite", method = RequestMethod.POST)
    public ResponseEntity<Site> createSite(@RequestBody Site input){
        Site site = new Site(input.getTitle(),input.getUrl());
        siteRepository.save(site);
        return new ResponseEntity<>(site, HttpStatus.OK);
    }

    @PostMapping("/updateSite")
    public ResponseEntity updateSite(@RequestBody Site input){
        List<Site> sites = (List<Site>) siteRepository.findAll();
        for (Site s:sites) {
            if (s.getId() == input.getId()) {
                s.setTitle(input.getTitle());
                s.setUrl(input.getUrl());
                siteRepository.save(s);
                break;
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // -------------------Retrieve All Sites---------------------------------------------

    @RequestMapping(value = "/sites", method = RequestMethod.GET)
    public ResponseEntity<List<Site>> listAllSites() {
        List<Site> sites = (List<Site>) siteRepository.findAll();
        return new ResponseEntity<List<Site>>(sites, HttpStatus.OK);
    }

    @RequestMapping(value = "/instances", method = RequestMethod.GET)
    public ResponseEntity<List<InstanceSite>> listAllInstanceForSite(@RequestParam("id") Long ident) {
        List<InstanceSite> res = new ArrayList<>();
        List<Site> sites = (List<Site>) siteRepository.findAll();
        for (Site s :sites) {
            if (s.getId() == ident)
                res = s.getInstanceSiteList();
        }
        return new ResponseEntity<List<InstanceSite>>(res, HttpStatus.OK);
    }


    @PostMapping("/createInstance")
    public ResponseEntity<InstanceSite> createInstanceForSite(@RequestBody InstanceSite input, @RequestParam("id") Long ident){
        Date date = new Date();
        InstanceSite i = new InstanceSite(input.getName(),input.getKeyName(),input.getStatus(),date);
        List<Site> sites = siteRepository.findAll();
        for (Site s :sites) {
            if (s.getId() == ident) {
                i.setSite(s);
                break;
            }
        }
        instanceSiteRepository.save(i);
        return new ResponseEntity<>(i, HttpStatus.OK);
    }

    @RequestMapping(value = "/elements", method = RequestMethod.GET)
    public ResponseEntity<List<Element>> listAllElementsForSite(@RequestParam("id") Long ident) {
        Site s = siteRepository.findOne(ident);
        List<Element> res = s.getElementList();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/deleteElement")
    public ResponseEntity removeElementForSite(@RequestBody Element element, @RequestParam("id") Long ident) {
        Site s = siteRepository.findOne(ident);
        if(s == null) {
            return ResponseEntity.notFound().build();
        }

        elementRepository.delete(element);
        return ResponseEntity.ok().build();
    }

//    TODO: зачем-то этот метод возвращает значения
//    TODO: try catch for findOne()
    @PostMapping("/saveElements")
    public ResponseEntity<List<Element>> saveElementsForSite(@RequestBody List<Element> elements, @RequestParam("id") Long ident){
        List<Element> e = new ArrayList<>();
        Site s = siteRepository.findOne(ident);
        for (Element el : elements) {
            if(el.getId() != null){
                Element tmp = elementRepository.findOne(el.getId());
                tmp.setKey(el.getKey());
                tmp.setName(el.getName());
                elementRepository.save(tmp);
            } else{
                e.add(new Element(el.getName(),el.getKey()));
            }
        }

        if (s != null){
//            List<Element> all = s.getElementList();
//            List<Element> tmp = new ArrayList<>();
//            for (Element allEl:all) {
//                boolean delFlag = true;
//                for (Element el:elements) {
//                    if (allEl.getId() == el.getId()){
//                        delFlag = false;
//                        break;
//                    }
//                }
//                if(delFlag)
//                    tmp.add(allEl);
//            }
//
//            tmp.forEach(s::deleteElement);

            e.forEach(x ->
            {
                x.setSite(s);
                elementRepository.save(x);
            });
        }

        return new ResponseEntity<>(e, HttpStatus.OK);
    }


}
