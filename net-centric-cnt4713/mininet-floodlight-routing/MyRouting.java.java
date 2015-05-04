//Group members
//Name1: Miguel Chateloin ID1: 3558744

package net.floodlightcontroller.myrouting;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.devicemanager.IDevice;
import net.floodlightcontroller.devicemanager.IDeviceService;
import net.floodlightcontroller.devicemanager.SwitchPort;

import java.util.ArrayList;
import java.util.Set;

import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.routing.Link;
import net.floodlightcontroller.routing.Route;
import net.floodlightcontroller.routing.RouteId;
import net.floodlightcontroller.staticflowentry.IStaticFlowEntryPusherService;
import net.floodlightcontroller.topology.ITopologyService;
import net.floodlightcontroller.topology.NodePortTuple;

import org.openflow.util.HexString;
import org.openflow.util.U8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyRouting implements IOFMessageListener, IFloodlightModule {

	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	protected IDeviceService deviceProvider;
	protected ILinkDiscoveryService linkProvider;

	protected Map<Long, IOFSwitch> switches;
	protected Map<Link, LinkInfo> links;
	protected Collection<? extends IDevice> devices;

	protected static int uniqueFlow;
	protected ILinkDiscoveryService lds;
	protected ITopologyService topology;
	protected IStaticFlowEntryPusherService flowPusher;
	protected boolean printedTopo = false;
	
	protected HashMap<Long, LinkedList<Long>> switchTopology;
	protected HashMap<Integer, Integer> switchIPToID;
	
	protected class SwitchVertex implements Comparable<SwitchVertex>{
		public long id;
		public int port;
		public int cost;
		SwitchVertex prev;
		
		public SwitchVertex(long id){
			this(id, 0, Integer.MAX_VALUE);
		}
		
		public SwitchVertex(long id, int port){
			this(id, port, Integer.MAX_VALUE);
		}
		
		public SwitchVertex(long id, int port, int cost){
			this(id, port, cost, null);
		}
		
		public SwitchVertex(long id, int port, int cost, SwitchVertex prev){
			this.id = id;
			this.port = port;
			this.cost = cost;
			this.prev = prev;
		}
		
		@Override
		public int compareTo(SwitchVertex other){
			return Integer.valueOf(cost).compareTo(other.cost);
		}
	}
	
	@Override
	public String getName() {
		return MyRouting.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		return (type.equals(OFType.PACKET_IN)
				&& (name.equals("devicemanager") || name.equals("topology")) || name
					.equals("forwarding"));
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		return false;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		l.add(IDeviceService.class);
		l.add(ILinkDiscoveryService.class);
		l.add(ITopologyService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context
				.getServiceImpl(IFloodlightProviderService.class);
		deviceProvider = context.getServiceImpl(IDeviceService.class);
		linkProvider = context.getServiceImpl(ILinkDiscoveryService.class);
		flowPusher = context
				.getServiceImpl(IStaticFlowEntryPusherService.class);
		lds = context.getServiceImpl(ILinkDiscoveryService.class);
		topology = context.getServiceImpl(ITopologyService.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) {
		//topology.addListener(this);
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {

		// eth is the packet sent by a switch and received by floodlight.
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx,
				IFloodlightProviderService.CONTEXT_PI_PAYLOAD);

		// We process only IP packets of type 0x0800.
		if (eth.getEtherType() != 0x0800) {
			return Command.CONTINUE;
		}
		else{
			// Print the topology if not yet.
			if (!printedTopo && lds != null) {
				switchIPToID = new HashMap<Integer, Integer>();
				printedTopo = true;
				
				System.out.println("*** Print topology");
				
				links = new HashMap<Link, LinkInfo>();
				
				if(lds.getLinks() != null) {
					links.putAll(lds.getLinks());
				}
				
				switchTopology = new HashMap<Long, LinkedList<Long>>();
				for(Link edge : links.keySet()){
					LinkedList<Long> paths = switchTopology.get(edge.getSrc());
										
					if(paths == null){
						switchTopology.put(edge.getSrc(), new LinkedList<Long>());
					}
					
					switchTopology.get(edge.getSrc()).add(edge.getDst());
				}
				
				String topologyOutput = "";
				for(Long src : switchTopology.keySet()){
					String lineNeighborsOutput = String.format("switch %d neighbors: ", src);
					for(Long dst : switchTopology.get(src)){
						lineNeighborsOutput += String.format(" %d,", dst);
					}
					topologyOutput += lineNeighborsOutput.substring(0, lineNeighborsOutput.length() - 1) + "\n";
				}
				System.out.print(topologyOutput);
			}

			// Parse the packet and obtained source and destination IPs.
			OFPacketIn pi = (OFPacketIn)msg;
			OFMatch match = new OFMatch();
		    match.loadFromPacket(pi.getPacketData(), pi.getInPort());	
			Integer srcIP = match.getNetworkSource();
			Integer dstIP = match.getNetworkDestination();
			
			byte[] aHexList = ByteBuffer
					.allocate(4)
					.order(ByteOrder.BIG_ENDIAN)
					.putInt(match.getNetworkSource())
					.array();
			byte[] bHexList = ByteBuffer
					.allocate(4)
					.order(ByteOrder.BIG_ENDIAN)
					.putInt(match.getNetworkDestination())
					.array();
			
			int sidA = (int)aHexList[3];
			int sidB = (int)bHexList[3];
			switchIPToID.put(srcIP, sidA);
			switchIPToID.put(dstIP, sidB);
			
			String srcIPReadable = String.format("%d.%d.%d.%d", 
				(int)aHexList[0],
				(int)aHexList[1],
				(int)aHexList[2],
				(int)aHexList[3]);
			
			String dstIPReadable = String.format("%d.%d.%d.%d", 
				(int)bHexList[0],
				(int)bHexList[1],
				(int)bHexList[2],
				(int)bHexList[3]);
				
			System.out.println(String.format("***New packet\nsrcIP: %s\ndstIP: %s", srcIPReadable, dstIPReadable));
			
			
			// Calculate the shortest path.
			Route route = shortestPath(srcIP, dstIP);
			if (route == null) return Command.CONTINUE;
			String routeOutput = "route: ";
			for(NodePortTuple node : route.getPath()){
				routeOutput += node.getNodeId() + " ";
			}
			System.out.println(routeOutput);
			
			
			// Write the path into the flow tables of the switches on the path.
			installRoute(route.getPath(), match);
			
			return Command.STOP;
		}
	}

	private Route shortestPath(Integer srcIP, Integer dstIP) {
		// TODO Auto-generated method stub
		PriorityQueue<SwitchVertex> vertices = new PriorityQueue<SwitchVertex>();
		HashMap<Long, SwitchVertex> tempLookup = new HashMap<Long, SwitchVertex>();
		
		SwitchVertex start = new SwitchVertex(switchIPToID.get(srcIP).longValue(), 0, 0);
		vertices.add(start);
		tempLookup.put(start.id, start);
		for(Long sid : switchTopology.keySet()){
			if(sid != start.id){
				SwitchVertex v = new SwitchVertex(sid);
				vertices.add(v);
				tempLookup.put(v.id, v);
			}
		}
		
		while(!vertices.isEmpty()){
			SwitchVertex chosen = vertices.poll();
			for(Long neighbor : switchTopology.get(chosen.id)){
				int costToTraverse = chosen.cost + 1;
				SwitchVertex sNeighbor = tempLookup.get(neighbor);
				if(costToTraverse < sNeighbor.cost){
					sNeighbor.cost = costToTraverse;
					sNeighbor.prev = chosen;
					vertices.remove(sNeighbor);
					vertices.add(sNeighbor);
				}
			}
		}
		
		LinkedList<NodePortTuple> resultPath = new LinkedList<NodePortTuple>();
		int sidEnd = switchIPToID.get(dstIP);
		SwitchVertex end = tempLookup.get((long)sidEnd);
		SwitchVertex v = end;
		while(v.prev != null){
			resultPath.addFirst(new NodePortTuple(v.id, v.port));
			v = v.prev;
		}
		
		Route path = new Route(start.id, end.id);
		path.setPath(resultPath);
		return path;
	}

	// generate and place routing rules
	private void installRoute(List<NodePortTuple> temp, OFMatch match) {

		OFMatch stoTMatch = new OFMatch();

		stoTMatch.setDataLayerType(Ethernet.TYPE_IPv4)
				.setNetworkSource(match.getNetworkSource())
				.setNetworkDestination(match.getNetworkDestination());

		for (int indx = 0; indx <= temp.size() - 1; indx += 2) {
			short inport = temp.get(indx).getPortId();
			stoTMatch.setInputPort(inport);
			List<OFAction> actions = new ArrayList<OFAction>();
			OFActionOutput outport = new OFActionOutput(temp.get(indx + 1)
					.getPortId());
			actions.add(outport);

			OFFlowMod stoDst = (OFFlowMod) floodlightProvider
					.getOFMessageFactory().getMessage(OFType.FLOW_MOD);
			stoDst.setCommand(OFFlowMod.OFPFC_ADD)
					.setIdleTimeout((short) 0)
					.setHardTimeout((short) 0)
					.setMatch(stoTMatch)
					.setPriority((short) 105)
					.setActions(actions)
					.setLength(
							(short) (OFFlowMod.MINIMUM_LENGTH + OFActionOutput.MINIMUM_LENGTH));
			flowPusher.addFlow("routeFlow" + uniqueFlow, stoDst,
					HexString.toHexString(temp.get(indx).getNodeId()));
			uniqueFlow++;
		}
	}
}
