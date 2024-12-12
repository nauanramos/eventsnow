package eventsnow.controllers;

import eventsnow.dtos.EventsRecordDto;
import eventsnow.models.EventsModel;
import eventsnow.repositories.EventsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@RestController
public class EventsController {


    private final EventsRepository eventsRepository;

    public EventsController(EventsRepository eventsRepository){
        this.eventsRepository = eventsRepository;
    }

    @PostMapping("/events")
    public ResponseEntity<EventsModel> saveEvents(@RequestBody @Valid EventsRecordDto eventsRecordDto) {
        var eventsModel = new EventsModel();
        BeanUtils.copyProperties(eventsRecordDto, eventsModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventsRepository.save(eventsModel));

    }
    @GetMapping("/events")
    public ResponseEntity<List<EventsModel>> getALLEvents(){
        List<EventsModel> eventsList = eventsRepository.findAll();
        if(!eventsList.isEmpty()){
            for(EventsModel events : eventsList){
                UUID id = events.getIdEvent();
                events.add(linkTo(methodOn(EventsController.class).getOneEvents(id)).withSelfRel());
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(eventsList);

    }
    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getOneEvents(@PathVariable(value="id") UUID id){
        Optional<EventsModel> eventsO = eventsRepository.findById(id);
        if(eventsO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Events not found. ");
        }
        eventsO.get().add(linkTo(methodOn(EventsController.class).getALLEvents()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(eventsO.get());

    }
    @PutMapping("/events/{id}")
    public ResponseEntity<Object> updateEvents(@PathVariable(value="id") UUID id,
                                               @RequestBody @Valid EventsRecordDto eventsRecordDto) {
        Optional<EventsModel> eventsO = eventsRepository.findById(id);
        if(eventsO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Events not found. ");
        }
        var eventsModel = eventsO.get();
        BeanUtils.copyProperties(eventsRecordDto, eventsModel);
        return ResponseEntity.status(HttpStatus.OK).body(eventsRepository.save(eventsModel));

    }
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Object> deleteEvents(@PathVariable(value="id") UUID id){
        Optional<EventsModel> eventsO = eventsRepository.findById(id);
        if(eventsO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Events not found. ");
        }
        eventsRepository.delete(eventsO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Events deleted successfully.");

    }
}