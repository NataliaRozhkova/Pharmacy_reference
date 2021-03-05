package pharmacy.reference.spring_server.util;

import pharmacy.reference.spring_server.entitis.Medicine;

import java.io.Serializable;
import java.util.List;

public class MedicineGrid implements Serializable {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<Medicine> medicines;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }
}
