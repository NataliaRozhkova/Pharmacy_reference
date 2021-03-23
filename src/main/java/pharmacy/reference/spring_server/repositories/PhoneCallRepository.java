package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entitis.PhoneCall;
import pharmacy.reference.spring_server.entitis.Statistic;

import java.util.Date;
import java.util.List;

public interface PhoneCallRepository extends JpaRepository<PhoneCall, Long> {

    @Query("SELECT b from  PhoneCall b   WHERE b.date >= :start_date AND b.date<= :finish_date")
    List< PhoneCall> findByPeriod(@Param("start_date") Date startDate, @Param("finish_date") Date finishDate);

    @Query("SELECT b from  PhoneCall b   WHERE b.date = :date ")
    List< PhoneCall> findByDate(@Param("date") Date Date);
}
