package com.thinkbiganalytics.nifi.rest.model.visitor;

import org.apache.nifi.web.api.dto.ProcessorDTO;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sr186054 on 2/14/16.
 */
public class NifiVisitableProcessor implements  NifiVisitable {


    private Set<NifiVisitableProcessor> sources; //parents
    private Set<NifiVisitableProcessor> destinations; //children

    private String id;

    private String inputPortId;

    private String outputPortId;

    private boolean isFailureProcessor;

    private ProcessorDTO dto;
    public NifiVisitableProcessor(ProcessorDTO dto) {
        this.dto = dto;
        this.id = dto.getId();
    }

    @Override
    public void accept(NifiFlowVisitor nifiVisitor) {
       nifiVisitor.visitProcessor(this);
    }
    public void addSource(NifiVisitableProcessor dto){
        getSources().add(dto);
    }

    public void addDestination(NifiVisitableProcessor dto) {
        getDestinations().add(dto);
    }

    public Set<NifiVisitableProcessor> getSources() {
        if(sources == null){
            sources = new HashSet<>();
        }
        return sources;
    }

    public void setSources(Set<NifiVisitableProcessor> sources) {
        this.sources = sources;
    }

    public Set<NifiVisitableProcessor> getDestinations() {
        if(destinations == null){
            destinations = new HashSet<>();
        }
        return destinations;
    }

    public void setDestinations(Set<NifiVisitableProcessor> destinations) {
        this.destinations = destinations;
    }

    public ProcessorDTO getDto() {
        return dto;
    }

    public void setDto(ProcessorDTO dto) {
        this.dto = dto;
    }


    public boolean isStart(){
        return !getDestinations().isEmpty() && getSources().isEmpty();
    }

    public  boolean isEnd(){
        return !getSources().isEmpty() && getDestinations().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NifiVisitableProcessor processor = (NifiVisitableProcessor) o;

        return id.equals(processor.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void print(){
        System.out.println(getDto().getName());
        for(NifiVisitableProcessor child: getDestinations()){
           child.print();
        }
    }
    //used for connections with input/output ports

    public Set<ProcessorDTO> getFailureProcessors(){
        Set<ProcessorDTO> failureProcessors = new HashSet<ProcessorDTO>();
        if(this.isFailureProcessor()){
            failureProcessors.add(this.getDto());
        }
        for(NifiVisitableProcessor child: getDestinations()){
            failureProcessors.addAll(child.getFailureProcessors());
        }
        return failureProcessors;
    }


    public boolean isFailureProcessor() {
        return isFailureProcessor;
    }

    public void setIsFailureProcessor(boolean isFailureProcessor) {
        this.isFailureProcessor = isFailureProcessor;
    }

    public String getInputPortId() {
        return inputPortId;
    }

    public void setInputPortId(String inputPortId) {
        this.inputPortId = inputPortId;
    }

    public String getOutputPortId() {
        return outputPortId;
    }

    public void setOutputPortId(String outputPortId) {
        this.outputPortId = outputPortId;
    }
}
