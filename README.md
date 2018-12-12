
# Transactional_system
A simple money transaction system implemented in Java



A simple transactional system where participants can transfer funds to each other. App can store all the data and transactional history in the database and load an initial state from external source. 
After loading data from the external source, the app initializes the database. 
After initializing the app supports creation of new transactions with given participants by ensuring a secure way of transfering funds ( check if an account holds enough money).

External source properties:
-excel file with a certain number of sheets - each sheet holds a name of an account owner
-content of each sheet holds a transactional history for its owner (row 
(RECIEVER - name of the receiving account owner, AMOUNT - >0 money received, <0 money sent))
-the first row in each sheet is original balance that will have BANK as the sender


Table BALANCE holds a current account balance for all participants