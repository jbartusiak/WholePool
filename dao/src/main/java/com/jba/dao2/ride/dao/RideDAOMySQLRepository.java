package com.jba.dao2.ride.dao;


import com.jba.dao2.ride.enitity.OfferedRides;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.user.enitity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@EnableTransactionManagement
public class RideDAOMySQLRepository implements RideDAO{

    private final static Logger logger = Logger.getLogger(RideDAOMySQLRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    public Ride getRideById(int id){
        Session session = sessionFactory.getCurrentSession();

        try{
            Ride result = session.
                    createQuery("from Ride r where r.id=:id", Ride.class).
                    setParameter("id", id).
                    getSingleResult();

            return result;
        }
        catch(NoResultException e){
            logger.info("No item found of id "+id);
            return null;
        }
        catch (Exception e){
            logger.error("Error getting ride of id "+id, e);
            throw e;
        }
    }

    public List<Ride> getAllRides(){
        Session session = sessionFactory.getCurrentSession();

        try{
            return session.
                    createQuery("From Ride", Ride.class).
                    getResultList();
        }
        catch (NoResultException e){
            logger.info("There are no rides registered yet!");
            return new ArrayList<>();
        }
        catch (Exception e){
            logger.error("Error getting all rides", e);
            throw e;
        }
    }

    public List<Ride> findRideByCriteria(
            Route route,
            @Nullable Date dateOfDeparture,
            @Nullable Date dateOfArrival
    ){
        RideDetails rideDetails = new RideDetails();

        if(dateOfDeparture!=null) rideDetails.setDateOfDeparture(dateOfDeparture);
        if(dateOfArrival!=null) rideDetails.setDateOfArrival(dateOfArrival);

        Session session = sessionFactory.getCurrentSession();

        try{
            List<Ride> result = session.
                    createQuery(
                            "select rd.rideId from RideDetails rd where rd.rideId.routeForThisRide=:route and (rd.dateOfDeparture=:dod or rd.dateOfArrival=:doa)",
                            Ride.class).
                    setParameter("doa", rideDetails.getDateOfArrival()).
                    setParameter("dod", rideDetails.getDateOfDeparture()).
                    setParameter("route", route).
                    getResultList();

            return result;
        }
        catch (NoResultException e){
            logger.info("There are no entries of given criteria");
            return new ArrayList<Ride>();
        }
        catch (Exception e){
            logger.error("Error retrieving entries!", e);
            throw e;
        }
    }

    public RideDetails getRideDetials(Ride ride){
        Session session = sessionFactory.getCurrentSession();

        try{
            session.beginTransaction();

            RideDetails details = session
                    .createQuery("from RideDetails rd where rd.rideId=:ride", RideDetails.class)
                    .setParameter("ride",ride)
                    .getSingleResult();

            session.getTransaction().commit();

            session.close();

            return details;
        }
        catch (NoResultException e){
            logger.info("There are no rides of this id!");
            if(session.isOpen())
                session.close();
            return null;
        }
        catch (Exception e){
            logger.error("Error occured while trying to fetch ride retails.");
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public Ride addRide(User offerer, Ride ride){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(ride);
        OfferedRides offeredRides = new OfferedRides(ride,offerer);
        session.saveOrUpdate(offeredRides);
        return ride;
    }

    public RideDetails addRideDetails(Ride ride, RideDetails rideDetails){
        rideDetails.setRideId(ride);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(rideDetails);
        return rideDetails;
    }

    public Ride deleteRide(Ride ride){
        logger.info("Deleting the ride itself");

        Session session = sessionFactory.getCurrentSession();

        try{
            session.delete(ride);
            return ride;
        }
        catch (Exception e){
            logger.error("Error occured while trying delete this ride", e);
            throw e;
        }
    }

    @Override
    public Ride deleteOfferedRides(Ride ride) {
        logger.info("Deleting user-ride associations");

        Session session = sessionFactory.getCurrentSession();

        try{
            List<OfferedRides> offeredRidesList = session.
                    createQuery("from OfferedRides r where r.ride=:ride", OfferedRides.class).
                    setParameter("ride", ride).
                    getResultList();

            offeredRidesList.forEach(offeredRide -> session.delete(offeredRide));
        }
        catch(NoResultException e){
            logger.info("No user-ride associations found for this ride");
        }
        catch (Exception e){
            logger.error("Error occured deleting user-ride associations",e);
            throw e;
        }

        return ride;
    }

    @Override
    public Ride removePassengers(Ride ride) {
        Session session = sessionFactory.getCurrentSession();

        try{
            List<RidePassangers> passangersList = session.
                    createQuery("from RidePassangers pas where pas.ride=:ride", RidePassangers.class).
                    setParameter("ride",ride).
                    getResultList();

            for(RidePassangers passanger:passangersList){
                session.delete(passanger);
            }
        }
        catch (NoResultException e){
            logger.info("There are no passengers for this ride");
        }
        catch (Exception e){
            logger.error("Error deleting passangers for this ride", e);
            throw e;
        }
        return ride;
    }

    @Override
    public Ride deleteRideDetails(Ride ride) {
        logger.info("Deleting ride details if present");

        Session session = sessionFactory.getCurrentSession();

        try{
            if(ride.getRideDetails()!=null) {
                session.delete(ride.getRideDetails());
            }
        }
        catch (Exception e){
            logger.error("Error occured deleting ride details",e);
            throw e;
        }
        return ride;
    }

    @Override
    public List<User> getRidePassengers(Ride ride) {
        Session session = sessionFactory.getCurrentSession();

        return session.
                createQuery("select rp.passenger from RidePassangers rp where rp.ride=:ride", User.class).
                setParameter("ride", ride).
                getResultList();
    }

    public RidePassangers registerToRide(User user, Ride ride) throws UnsupportedOperationException{
        Session session = sessionFactory.getCurrentSession();
        try{
            long count = session.
                    createQuery("select count(rp) from RidePassangers rp where rp.ride=:ride", Long.class).
                    setParameter("ride", ride).
                    getSingleResult();

            if(count>=ride.getNrOfSeats()){
                throw new UnsupportedOperationException("No more places");
            }

            RidePassangers ridePassangers = new RidePassangers(user,ride);

            session.save(ridePassangers);
            return ridePassangers;
        }
        catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("There are no more free places for this ride!");
        }
    }

    public RidePassangers unregisterFromRide(User user, Ride ride){
        Session session = sessionFactory.getCurrentSession();

        try{
            RidePassangers rp = session.
                    createQuery("from RidePassangers rp where rp.ride=:ride and rp.passenger=:user", RidePassangers.class).
                    setParameter("ride", ride).
                    setParameter("user", user).
                    getSingleResult();

            session.delete(rp);
            return rp;
        }
        catch (NoResultException e){
            logger.info("User "+user+" is not registered for ride "+ride);
            throw new NoResultException("User "+user.getUserId()+" is not registered to ride "+ride.getRideId());
        }
        catch (Exception e){
            logger.error("Error occured deleting passanger!", e);
            throw e;
        }
    }

    public List<Ride> getRidesByUser(User user){
        Session session = sessionFactory.getCurrentSession();

        try{
            List<OfferedRides> offeredRides = session.
                    createQuery("From OfferedRides o where o.offerer=:user", OfferedRides.class).
                    setParameter("user",user).
                    getResultList();

            List<Ride> result = new ArrayList<>();

            for (OfferedRides offer: offeredRides){
                result.add(offer.getRide());
            }

            return result;
        }
        catch (NoResultException e){
            logger.info("User "+user+" did not offer any rides!");
            throw new NoResultException("User "+user.getUserId()+" did not offer any rides!");
        }
    }

    @Override
    public User getRideOfferer(Ride ride) {
        Session session = sessionFactory.getCurrentSession();

        OfferedRides offeredRides = session.
                createQuery("from OfferedRides o where o.ride=:ride", OfferedRides.class).
                setParameter("ride", ride).
                getSingleResult();

        return offeredRides.getOfferer();
    }
}