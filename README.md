# SFC ORCHESTRATOR

This project is the first version of SFC Orchestrator that provides SFC Control layer as stated in [IETF SFC working group specs] (https://datatracker.ietf.org/doc/draft-ietf-sfc-control-plane/) and the SFC Management Plane in [ONF SFC specs](https://www.opennetworking.org/images/stories/downloads/sdn-resources/onf-specifications/L4-L7_Service_Function_Chaining_Solution_Architecture.pdf). It manages the Network Service Requests occured by the NFV Orchestrator - [Open Baton](http://openbaton.github.io/) - and translates these requests which include VNF Forwarding Graphs into Service Function Chains and Paths using OpenDaylight SDN Control - [SFC Project](https://wiki.opendaylight.org/view/Service_Function_Chaining:Main) - that provides SFC Data plane functionality.

## Prerequests 

1. Open Baton Zabbix plugin running (see the [doc of Zabbix plugin](https://github.com/openbaton/docs/blob/develop/docs/zabbix-plugin.md))
2. [Open Baton 3.0.1](https://github.com/openbaton/NFVO/tree/3.0.0) running
3. [Generic VNFM 3.0.1](https://github.com/openbaton/generic-vnfm/tree/3.0.1) running
4. [OPNFV Apex deployment with the SFC scenario](http://artifacts.opnfv.org/apex/docs/installation-instructions/index.html)
5. [mysql server](https://dev.mysql.com/doc/refman/5.7/en/linux-installation.html) running
In Case you need Fault Management:
6. [Open Baton Fault Management](https://github.com/openbaton/fm-system) running
In Case you need Auto Scaling:
7. [Open Baton AutoScaling Engine](https://github.com/openbaton/autoscaling-engine) running


## Create the database
You need root access to mysql-server in order to create a new database called sfco. Once you access into mysql, execute the following operation:
```
create database sfco;
```
Once the database has been created, you can create a user which will be used by the SFC Orchestrator to access and store data on the database. If you decide to use the root user you can skip this step. 

Grant the access to the database "sfco", to the user, running the following command:
```
GRANT ALL PRIVILEGES ON sfco.* TO sfcouser@'%' IDENTIFIED BY 'changeme';
```

Modify sfco.properties file in order to use different credentials for the database.

## Getting Started

After installing the prerequests, you can clone the SFC Orchestrator Project from git and compile it:
```
git clone https://github.com/mah88/sfc-orchestrator.git
```

## Configuration files description:
### application.properties

Parameter                                    | Description
---------------------------------------------|--------------------------------------------------------------------
SFCO.rabbitmq.management.port                | Rabbit MQ management port                   
server.port                                  | Port of the SFC Orchestrator
spring.http.converters.preferred-json-mapper | json converter. Default value: gson
spring.rabbitmq.host                         | IP of the machine where RabbitMQ is running. 
spring.rabbitmq.port                         | Port of the machine where RabbitMQ is running. Default value: 5672
spring.rabbitmq.username                     | Username for authorization of RabbitMQ
spring.rabbitmq.password                     | Password for authorization of RabbitMQ
logging.level.*                              | Log level of the defined modules

### sfco.properties

Parameter                                    | Description
---------------------------------------------|--------------------------------------------------------------------
nfvo.baseURL                                 |         NFV Orchestrator IP                      
nfvo.basePort                                | NFV Orchestrator Port. Default value: 8080
nfvo.username                                | NFVO Username. Default value: admin 
nfvo.password                                | NFVO Password. Default value: openbaton
sfc.driver                                   | SDN (SFC) Controller used for providing SFC
sfc.ip                                       | IP of the SFC Controller
sfc.port                                     | port of the SFC Controller
sfc.username                                 | Username for authorization of SDN Controller
sfc.password                                 | Password for authorization of SDN Controller
sfc.sf.deployment.schedulingType             | The Scheduler Algorithm at the Deployment Phase (supported types: random , Shortestpath, roundrobin, tradeoff)
sfc.sf.runtime.schedulingType                | The Scheduler Algorithm at the Runtime Phase (supported types:  qos-aware-loadbalancer , Shortestpath, roundrobin, tradeoff)
sf.monitoring.item                           | SF monitoring item from Zabbix (i.e. net.if.in[eth0],system.cpu.load[percpu,avg1])
openstack.ip                                 | IP of the VIM (OpenStack)
openstack.username                           | Username for authorization of OpenStack
openstack.password                           | Password for authorization of OpenStack
openstack.tenantname                         | Tenant name for OpenStack
spring.datasource.username                   | Username of mysql (default: root)
spring.datasource.password                   | Password of mysql 
spring.datasource.url                        | URL of mysql (default: jdbc:mysql://localhost:3306/sfco)
spring.datasource.driver-class-name          | Driver class name of mysql (default: com.mysql.jdbc.Driver)    
spring.jpa.database-platform                 | JPA Databse platform (default: org.hibernate.dialect.MySQLDialect)



## Starting the SFC Orchestrator
Starting the SFC Orchestrator using the provided script with the following command:

`````
./sfc-orch.sh start
`````

## How to use it 

This guide shows you how to let SFC Orchestrator applying the VNFFG - NFV Orchestrator requests. In particular, it describes how to define the VNFFG inside the Network Service Descriptor.

### Creating VNF Packages

Refering to [Open Baton VNF Packages usage](http://openbaton.github.io/documentation/vnfpackage/), you can use the following JSON File in your setup.

```json
{
    "vendor": "fokus",
    "version": "0.2",
    "name": "http-sf",
    "type": "http",
    "endpoint": "generic",
    "vdu": [
        {
            "vm_image": [
                "sf-image"
            ],
            "vimInstanceName": [
                "vim-instance-odl"
            ],
            "scale_in_out": 2,
            "vnfc": [
                {
                    "connection_point": [
                        {
                            "floatingIp":"random",
                            "virtual_link_reference": "test-network"
                        }
                    ]
                }
     
            ]
        }
    ]
      ,
    "virtual_link": [
        {
            "name": "test-network"
        }
    ],
    "lifecycle_event": [
        {
            "event": "INSTANTIATE",
            "lifecycle_events": [
                "configure.sh",
                "start_vxlan.sh"
            ]
        }
    ],
    "deployment_flavour": [
        {
            "df_constraint": [
                "constraint1",
                "constraint2"
            ],
            "costituent_vdu": [],
            "flavour_key": "m1.custom"
        }
    ],
    "vnfPackageLocation": "https://github.com/mah88/SF_VNF_scripts.git"
}

```
and the following Metadata.yaml file as well:

```
name: http-sf
scripts-link: https://github.com/mah88/SF_VNF_scripts.git
vim_types:
         - openstack
image:
    upload: false
    names: 
         - sf-image

```

### Creating the Network Service Descriptor:

Here is the NSD that you can use. It includes the VNF Forwarding Graph Descriptor which includes the Network Forwarding Path and the Policy dedicated to it. 
The VNFD part of the NSD includes the ids of the VNF Descriptors which are involved in this NSD. The VNFFGD the dependent virtual network links, the Network Forwarding Path and the Involved VNFDs in this VNFFGD. The Network Forwarding Path consists of connection and policy. The connection constructs the order of the Service Functions (VNFs) in the Service Function Chain. The policy consists of the ACL matching criteria that is used to classify the traffic and directs the matched ones to this chain. The policy is also contains qos_level which is used for identifing the QoS for each chain.

```json
{
    "name": "http-sfc",
    "vendor": "fokus",
    "version": "0.1-ALPHA",
    "vnfd": [
        {
            "id": "aab08537-f8cf-4712-8805-ebd02f6069c9"
        },
        {
            "id": "bf4b5891-b068-4242-ac14-6e0b62a6d952"
        }
    ],
    "vnffgd": [
        
        {
            "vendor": "fokus",
            "version": "0.1-ALPHA",
            "number_of_endpoints": 1,
            "symmetrical":false,
            "dependent_virtual_link": [
                {
                    "name": "test-network"
                }
            ],
            "network_forwarding_path": [
                {
                    "connection": {
                        "0": "http-sf",   
                        "1": "fw-sf"
                                 
                    },
                    "policy": {
                        "acl_matching_criteria": {
                            "destination_port":80,
                            "source_port":0,
                            "protocol": 6,
                            "source_ip":"12.0.0.4/32"
                        },
                      "qos_level":"bronze"
                    }
                }
            ]
        }
    ],
    "vld": [
        {
            "name": "test-network"
        }
    ],
    "vnf_dependency": []
}
````

Now all is ready and when you launch the NSD, the VNFs will be deployed in the cloud network and an Event of Instantiate Finish will appears at the NFV Orchestrator which means that the SFC Orchestrator will start deploying the chain in the network.



