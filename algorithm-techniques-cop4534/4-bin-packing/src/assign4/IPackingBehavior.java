/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.util.Collection;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 * @param <Bin>
 */
public interface IPackingBehavior<Bin> {
    
    public String getName();
    
    public Collection<Bin> solveOnline(ItemReader input);
    
    public Collection<Bin> solveOffline(ItemReader input);
    
}
