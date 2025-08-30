insert into vehicle(id, model, status, vehicle_year, brand, engine) values(1, 'shouldCreateBooking', 'AVAILABLE', 2018, 'Fiat', 1.0);
insert into vehicle(id, model, status, vehicle_year, brand, engine) values(2, 'shouldNotCreateBookingWithStartDateInThePast', 'AVAILABLE', 2018, 'Fiat', 1.0);
insert into vehicle(id, model, status, vehicle_year, brand, engine) values(3, 'shouldNotCreateBookingWithEndDateBeforeStartDate', 'RENTED', 2018, 'Fiat', 1.0);
insert into vehicle(id, model, status, vehicle_year, brand, engine) values(4, 'shouldNotCreateBookingWithUnavailableVehicle', 'AVAILABLE', 2018, 'Fiat', 1.0);

insert into booking(id, vehicleId, customerName, startDate, endDate, status) values(1, 1, 'ShouldCancelBookingWithStatusCreated', '2025-11-10', '2025-11-17', 'CREATED');
insert into booking(id, vehicleId, customerName, startDate, endDate, status) values(2, 2, 'ShouldNotCancelBookingThatIsNotCreated', '2025-11-10', '2025-11-17', 'CANCELED');
insert into booking(id, vehicleId, customerName, startDate, endDate, status) values(3, 3, 'ShouldFinishBookingThatIsCreated', '2025-11-10', '2025-11-17', 'CREATED');