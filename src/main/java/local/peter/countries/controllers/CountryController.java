package local.peter.countries.controllers;

import local.peter.countries.models.Country;
import local.peter.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController {
    @Autowired
    CountryRepository countryrepos;

    private List<Country> findCountries(List<Country> countryList, CheckCountry tester) {
        List<Country> temporary = new ArrayList<>();

        for(Country cx : countryList) {
            if(tester.test(cx)) {
                temporary.add(cx);
            }
        }

        return temporary;
    }
    //http://localhost:5280/names/al
    @GetMapping(value = "/names/all", produces = {"application/json"})
    public ResponseEntity<?> listAllCountries() {
        List<Country> countryList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(countryList::add);
        countryList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    @GetMapping(value = "/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> listAllByFirstName(@PathVariable char letter) {
        List<Country> countryList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(countryList::add);
        List<Country> filteredList = findCountries(countryList, c -> c.getName().charAt(0) == letter);
        filteredList.sort((f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));

        return new ResponseEntity<>(filteredList, HttpStatus.OK);
    }

    @GetMapping(value = "/population/total", produces = {"application/json"})
    public ResponseEntity<?> findTotalPopulation() {
        List<Country> countryList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(countryList::add);

        long totalPop = 0;
        for(Country turtle : countryList) {
            totalPop += turtle.getPopulation();
            System.out.println(turtle);
        }

        System.out.println("Total Population is: " + totalPop);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/population/min", produces = {"application/json"})
    public ResponseEntity<?> findSparsest() {
        List<Country> countryList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(countryList::add);
        countryList.sort((p1, p2) -> Long.compare(p1.getPopulation(), p2.getPopulation()));
        return new ResponseEntity<>(countryList.get(0), HttpStatus.OK);
    }

    @GetMapping(value = "/population/max", produces = {"application/json"})
    public ResponseEntity<?> findDensest() {
        List<Country> countryList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(countryList::add);
        countryList.sort((p1, p2) -> Long.compare(p2.getPopulation(), p1.getPopulation()));
        return new ResponseEntity<>(countryList.get(0), HttpStatus.OK);
    }

    @GetMapping(value = "/population/median", produces = {"application/json"})
    public ResponseEntity<?> findAverage() {
        List<Country> countryList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(countryList::add);
        countryList.sort((p1, p2) -> Long.compare(p2.getPopulation(), p1.getPopulation()));

        int totalCountries = countryList.size();
        System.out.println("Total Countries: " + totalCountries);
        int medianCountryId = (1 + totalCountries) / 2;
        System.out.println("Median Country ID is: " + medianCountryId);

        // Used (medianCountryId - 1) below since the position of an array is 1 less than the ID value
        // of the object
        // ALSO: Mathematically, Country ID of 101 is the median of a list of 201 countries.
        // Instructions on git-repo incorrectly assumes ID of 100.
        return new ResponseEntity<>(countryList.get(medianCountryId - 1), HttpStatus.OK);
    }
}
