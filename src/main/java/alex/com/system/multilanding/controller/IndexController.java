package alex.com.system.multilanding.controller;

import alex.com.system.multilanding.model.InstanceSite;
import alex.com.system.multilanding.model.Site;
import alex.com.system.multilanding.repository.InstanceSiteRepository;
import alex.com.system.multilanding.repository.SiteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class IndexController {

    private final SiteRepository siteRepository;
    private final InstanceSiteRepository instanceSiteRepository;

    public IndexController(SiteRepository siteRepository, InstanceSiteRepository instanceSiteRepository){
        this.siteRepository = siteRepository;
        this.instanceSiteRepository = instanceSiteRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/sites/{id}")
    public String site(@PathVariable("id") Long id){
        List<Site> sites = (List<Site>) siteRepository.findAll();
        for (Site s : sites) {
            if (s.getId() == id)
                return "site";
        }
        return "index";
    }
    @GetMapping("/sites/{id_site}/instances/{id_instance}")
    public String elements(@PathVariable("id_site") Long idSite, @PathVariable("id_instance") Long idInstance){
        try {
            Site s = siteRepository.findOne(idSite);
            InstanceSite is =instanceSiteRepository.findOne(idInstance);
            return "elements";
        }

        catch (Exception e){
            return "index";
        }
    }
}
