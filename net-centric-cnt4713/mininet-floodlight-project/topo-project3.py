"""Topology for project 3

Two directly connected switches plus two hosts for each switch:

   host 		     host
	\                   /
 	 \	           /
	  switch --- switch
	 /                 \
        /                   \
   host                      host

Adding the 'topos' dict with a key/value pair to generate our newly defined
topology enables one to pass in '--topo=project3' from the command line.

sudo mn --custom=~/mininet/custom/topo-project3.py --topo=project3 --link=tc --mac --switch ovsk --controller remote,ip=127.0.0.1,port6653
"""

from mininet.topo import Topo

class MyTopo( Topo ):
    "Topology for project 3."

    def __init__( self ):
        "Create custom topo."

        # Initialize topology
        Topo.__init__( self )

        # Add hosts and switches
	h1 = self.addHost( 'h1' )
	h2 = self.addHost( 'h2' )
	h3 = self.addHost( 'h3' )
	h4 = self.addHost( 'h4' )
	s1 = self.addSwitch( 's1' )
        s2 = self.addSwitch( 's2' )
	
	# Add links
	self.addLink(h1,s1, bw=10)
	self.addLink(h2,s1, bw=15)	
	self.addLink(s1,s2, bw=20)	
	self.addLink(s2,h3, bw=15)
	self.addLink(s2,h4, bw=5)


topos = { 'project3': ( lambda: MyTopo() ) }
