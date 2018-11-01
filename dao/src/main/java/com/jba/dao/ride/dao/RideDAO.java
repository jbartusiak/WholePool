package com.jba.dao.ride.dao;

import com.jba.dao.ride.enitity.OfferedRides;
import com.jba.dao.ride.enitity.Ride;
import com.jba.dao.ride.enitity.RideDetails;
import com.jba.dao.ride.enitity.RidePassangers;
import com.jba.dao.route.entity.Route;
import com.jba.dao.user.enitity.User;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class RideDAO {

    private final static Logger logger = Logger.getLogger(RideDAO.class);

    public static Ride getRideById(int id){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            Ride result = session.
                    createQuery("from Ride r where r.id=:id", Ride.class).
                    setParameter("id", id).
                    getSingleResult();

            session.getTransaction().commit();

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

    public static Set<Ride> getAllRides(){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            Set<Ride> rides = session.
                    createQuery("From Ride", Ride.class).
                    getResultStream().
                    collect(Collectors.toSet());

            session.getTransaction().commit();

            return rides;
        }
        catch (NoResultException e){
            logger.info("There are no rides registered yet!");
            return new HashSet<Ride>();
        }
        catch (Exception e){
            logger.error("Error getting all rides", e);
            throw e;
        }
    }

    public static Set<Ride> findRideByCriteria(
            Route route,
            Optional<Date> dateOfDeparture,
            Optional<Date> dateOfArrival
    ){
        RideDetails rideDetails = new RideDetails();

        dateOfDeparture.ifPresent(date -> rideDetails.setDateOfDeparture(date));
        dateOfArrival.ifPresent(date -> rideDetails.setDateOfArrival(date));

        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            Set<Ride> result = session.
                    createQuery(
                            "select rd.rideId from RideDetails rd where rd.dateOfArrival=:doa or rd.dateOfDeparture=:dod and rd.rideId.routeForThisRide=:route",
                            Ride.class).
                    setParameter("doa", rideDetails.getDateOfArrival()).
                    setParameter("dod", rideDetails.getDateOfDeparture()).
                    setParameter("route", route).
                    getResultStream().
                    collect(Collectors.toSet());

            session.getTransaction().commit();

            return result;
        }
        catch (NoResultException e){
            logger.info("There are no entries of given criteria");
            return new HashSet<Ride>();
        }
        catch (Exception e){
            logger.error("Error retrieving entries!", e);
            throw e;
        }
    }

    public static RideDetails getRideDetials(Ride ride){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            RideDetails details = session
                    .createQuery("from RideDetails rd where rd.rideId=:ride", RideDetails.class)
                    .setParameter("ride",ride)
                    .getSingleResult();

            return details;
        }
        catch (NoResultException e){
            logger.info("There are no rides of this id!");
            return null;
        }
        catch (Exception e){
            logger.error("Error occured while trying to fetch ride retails.");
            throw e;
        }
    }

    public Ride addRide(User offerer, Ride ride){
        DBUtils.saveOrUpdate(ride);
        OfferedRides offeredRides = new OfferedRides(ride,offerer);
        DBUtils.saveOrUpdate(offeredRides);
        return ride;
    }

    public RideDetails addRideDetails(Ride ride, RideDetails rideDetails){
        rideDetails.setRideId(ride);
        return (RideDetails) DBUtils.saveOrUpdate(rideDetails);
    }

    public static Ride deleteRide(Ride ride){
        logger.info("Deleting ride details if present");

        Session deleteRideDetailsSession = WPLSessionFactory.getDBSession();

        try(deleteRideDetailsSession){
            deleteRideDetailsSession.beginTransaction();

            deleteRideDetailsSession.delete(ride.getRideDetails());

            deleteRideDetailsSession.getTransaction().commit();
        }
        catch (Exception e){
            logger.error("Error occured deleting ride details",e);
            throw e;
        }

        logger.info("Deleting user-ride associations");

        Session searchForOfferedRidesAndDeleteThemSession = WPLSessionFactory.getDBSession();

        try(searchForOfferedRidesAndDeleteThemSession){
            searchForOfferedRidesAndDeleteThemSession.beginTransaction();
            List<OfferedRides> offeredRidesList = searchForOfferedRidesAndDeleteThemSession.
                    createQuery("from OfferedRides r where r.ride=:ride", OfferedRides.class).
                    setParameter("ride", ride).
                    getResultList();

            offeredRidesList.forEach(offeredRide -> searchForOfferedRidesAndDeleteThemSession.delete(offeredRide));

            searchForOfferedRidesAndDeleteThemSession.getTransaction().commit();
        }
        catch(NoResultException e){
            logger.info("No user-ride associations found for this ride");
        }
        catch (Exception e){
            logger.error("Error occured deleting user-ride associations",e);
            throw e;
        }

        logger.info("Deleting ride passengers");

        Session deletePassengersSession = WPLSessionFactory.getDBSession();

        try(deletePassengersSession){
            deletePassengersSession.beginTransaction();

            List<RidePassangers> passangersList = deletePassengersSession.
                    createQuery("from RidePassangers pas where pas.ride=:ride", RidePassangers.class).
                    setParameter("ride",ride).
                    getResultList();

            for(RidePassangers passanger:passangersList){
                deletePassengersSession.delete(passanger);
            }

            deletePassengersSession.getTransaction().commit();
        }
        catch (NoResultException e){
            logger.info("There are no passengers for this ride");
        }
        catch (Exception e){
            logger.error("Error deleting passangers for this ride", e);
            throw e;
        }

        logger.info("Deleting the ride itself");

        Session deleteRideSession = WPLSessionFactory.getDBSession();

        try(deleteRideSession){
            deleteRideSession.beginTransaction();

            deleteRideSession.delete(ride);

            deleteRideSession.getTransaction().commit();

            return ride;
        }
        catch (Exception e){
            logger.error("Error occured while trying delete this ride", e);
            throw e;
        }
    }

    public static RidePassangers registerToRide(User user, Ride ride) throws UnsupportedOperationException{
        Session session = WPLSessionFactory.getDBSession();
        try(session){
            session.beginTransaction();

            long count = session.
                    createQuery("select count(rp) from RidePassangers rp where rp.ride=:ride", Long.class).
                    setParameter("ride", ride).
                    getSingleResult();

            if(count>=ride.getNrOfSeats()){
                throw new UnsupportedOperationException("No more places");
            }

            RidePassangers ridePassangers = new RidePassangers(user,ride);

            session.save(ridePassangers);

            session.getTransaction().commit();

            return ridePassangers;
        }
        catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("There are no more free places for this ride!");
        }
    }

    public static RidePassangers unregisterFromRide(User user, Ride ride){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            RidePassangers rp = session.
                    createQuery("from RidePassangers rp where rp.ride=:ride and rp.passenger=:user", RidePassangers.class).
                    setParameter("ride", ride).
                    setParameter("user", user).
                    getSingleResult();

            session.delete(rp);

            session.getTransaction().commit();

            return rp;
        }
        catch (NoResultException e){
            logger.info("User "+user+" is not registered for ride "+ride);
            return null;
        }
        catch (Exception e){
            logger.error("Error occured deleting passanger!", e);
            throw e;
        }
    }

    public static Set<Ride> getRidesByUser(User user){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            List<OfferedRides> offeredRides = session.
                    createQuery("From OfferedRides o where o.offerer=:user", OfferedRides.class).
                    setParameter("user",user).
                    getResultList();

            Set<Ride> result = new HashSet<>();
            for(OfferedRides offer: offeredRides){
                result.add(offer.getRide());
            }

            session.getTransaction().commit();
            return result;
        }
        catch (NoResultException e){
            logger.info("User "+user+" did not offer any rides!");
            return new HashSet<Ride>();
        }
    }
}