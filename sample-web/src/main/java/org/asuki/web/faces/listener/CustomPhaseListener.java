package org.asuki.web.faces.listener;

import static javax.faces.event.PhaseId.ANY_PHASE;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.slf4j.Logger;

public class CustomPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Override
    public void afterPhase(PhaseEvent event) {
        log.info("afterPhase: " + event.getPhaseId());
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        log.info("beforePhase: " + event.getPhaseId());
    }

    @Override
    public PhaseId getPhaseId() {
        return ANY_PHASE;
    }

}
