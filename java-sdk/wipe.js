db = db.getSiblingDB('harness_meta_store')
db.dropDatabase()
db = db.getSiblingDB('harness_shared_user')
db.dropDatabase()
db = db.getSiblingDB('harness_test_shared_users')
db.dropDatabase()
db = db.getSiblingDB('hinting')
db.dropDatabase()
db = db.getSiblingDB('test_cb')
db.dropDatabase()
db = db.getSiblingDB('test_nh')
db.dropDatabase()
db = db.getSiblingDB('test_resource')
db.dropDatabase()
db = db.getSiblingDB('test_resource_2')
db.dropDatabase()
