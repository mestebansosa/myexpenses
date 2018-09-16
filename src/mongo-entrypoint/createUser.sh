echo "Creating mongo users..."
mongo myexpenses --host localhost -u root -p 123456 --authenticationDatabase admin --eval "db.createUser({user: 'mesteban', pwd: 'newuser1', roles:[{role:'dbOwner', db: 'myexpenses'}]});"
echo "Mongo users created."