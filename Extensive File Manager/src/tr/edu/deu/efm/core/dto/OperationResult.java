package tr.edu.deu.efm.core.dto;

import java.util.List;

public class OperationResult {
    private final boolean success;
    private final List<String> affectedItems;

    public OperationResult(boolean success, List<String> affectedItems) {
        this.success = success;
        this.affectedItems = affectedItems;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getAffectedItems() {
        return affectedItems;
    }
}