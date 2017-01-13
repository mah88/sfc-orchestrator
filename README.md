# SFC ORCHESTRATOR

This project is the first version of SFC Orchestrator that provides SFC Control layer as stated in [IETF SFC working group specs] (https://datatracker.ietf.org/doc/draft-ietf-sfc-control-plane/) and [ONF SFC specs](https://www.opennetworking.org/images/stories/downloads/sdn-resources/onf-specifications/L4-L7_Service_Function_Chaining_Solution_Architecture.pdf). It manages the Network Service Requests occured by the NFV Orchestrator - [Open Baton](http://openbaton.github.io/) - and translates these requests which include VNF Forwarding Graphs into Service Function Chains and Paths using OpenDaylight SDN Control - [SFC Project](https://wiki.opendaylight.org/view/Service_Function_Chaining:Main) - that provides SFC Data plane functionality.

## Prerequests 

1. Open Baton Zabbix plugin running (see the [doc of Zabbix plugin](https://github.com/openbaton/docs/blob/develop/docs/zabbix-plugin.md))
2. [Open Baton 2.0.1](https://github.com/openbaton/NFVO/tree/2.0.1) running
3. [Generic VNFM 2.0.1](https://github.com/openbaton/generic-vnfm/tree/2.0.1) running
4. [Open Baton Fault Management](https://github.com/openbaton/fm-system) running
5. [OVS 2.3.90 with NSH Patch](https://github.com/pritesh/ovs/blob/nsh-v8/third-party/start-ovs-deb.sh) running this script should  install ovs with its patch for Ubuntu only.
6. [Devstack Liberty](https://github.com/openstack-dev/devstack/tree/stable/liberty) running using this local.conf file. This local.conf file includes the plugin and installation of Opendaylight SFC.
 

## Getting Started

After installing the prerequests, you can clone the SFC Orchestrator Project from git and compile it:
```
git clone https://github.com/mah88/sfc-orchestrator.git
```

##Configuration file description

Parameter                                    | Description
---------------------------------------------|--------------------------------------------------------------------
nfvo.baseURL                                 |         NFV Orchestrator IP                      
nfvo.basePort                                | NFV Orchestrator Port. Default value: 8080
nfvo.username                                | NFVO Username. Default value: admin 
nfvo.password                                | NFVO Password. Default value: openbaton
nfvo.security                                | NFVO Security. Default value: false
server.port                                  | Port of the SFC Orchestrator
spring.http.converters.preferred-json-mapper | json converter. Default value: gson
spring.rabbitmq.host                         | IP of the machine where RabbitMQ is running. 
spring.rabbitmq.port                         | Port of the machine where RabbitMQ is running. Default value: 5672
spring.rabbitmq.username                     | Username for authorization of RabbitMQ
spring.rabbitmq.password                     | Password for authorization of RabbitMQ
logging.level.*                              | Log level of the defined modules


##Starting the SFC Orchestrator
Starting the SFC Orchestrator using the provided script with the following command:
`````
./sfc-orchestrator.sh start
`````

##How to use it 

This guide shows you how to let SFC Orchestrator applying the VNFFG - NFV Orchestrator requests. In particular, it describes how to define the VNFFG inside the Network Service Descriptor.

###Creating VNF Packages

Refering to [Open Baton VNF Packages usage](http://openbaton.github.io/documentation/vnfpackage/), you can use the following JSON File in your setup.

```json
{
    "vendor":"fokus",
    "version":"0.2",
    "name":"fw-SF",
    "type":"firewall",
    "endpoint":"generic",
    "vdu":[
        {
            "vm_image":[
                "sfc"
            ],
            "vimInstanceName":"vim-instance-odl",
            "scale_in_out":1,
            "vnfc":[
                {
                    "connection_point":[
                        {
                            
                            "virtual_link_reference":"NET_MGMT"
                        }
                    ]
                }
            ]
        }
    ],
    "virtual_link":[
        {
            "name":"NET_MGMT"
        }
    ],
    "lifecycle_event":[
        {
            "event":"INSTANTIATE",
            "lifecycle_events":[
             "install.sh",
             "install-srv.sh"
            ]
        }
        
    ],
    "deployment_flavour":[
        {
            "df_constraint":[
                "constraint1",
                "constraint2"
            ],
            "costituent_vdu":[
            ],
            "flavour_key":"custom"
        }
    ]
}

```
and the following Metadata.yaml file as well:

```
name: http-SF
image:
    upload: false
    names: 
         - sfc

```

###Creating the Network Service Descriptor:

Here is the NSD that you can use. It includes the VNF Forwarding Graph Descriptor which includes the Network Forwarding Path and the Policy dedicated to it. 
The VNFD part of the NSD includes the ids of the VNF Descriptors which are involved in this NSD. The VNFFGD the dependent virtual network links, the Network Forwarding Path and the Involved VNFDs in this VNFFGD. The Network Forwarding Path consists of connection and policy. The connection constructs the order of the Service Functions (VNFs) in the Service Function Chain. The policy consists of the ACL matching criteria that is used to classify the traffic and directs the matched ones to this chain.

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
                    "name": "NET_MGMT"
                }
            ],
            "network_forwarding_path": [
                {
                    "connection": {
                        "0": "fw-SF",
                        "1": "http-SF"
                    },
                    "policy": {
                        "acl_matching_Criteria": {
                            "destination_port": 80,
                            "protocol": 6
                        }
                    }
                }
            ],
            "constituent_vnfs": [
                {
                    "vnf_reference": "983fc2f0-f1b9-4bb4-8da2-5f041ad3a733",
                    "number_of_instances": 1
                }
            ]
        }
    ],
    "vld": [
        {
            "name": "NET_MGMT"
        }
    ],
    "vnf_dependency": []
}
````

Now all is ready and when you launch the NSD, the VNFs will be deployed in the cloud network and an Event of Instantiate Finish will appears at the NFV Orchestrator which means that the SFC Orchestrator will start deploying the chain in the network.



