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
 */
public class FirstFit implements IPackingBehavior<Bin>{

    public String getName(){
        return "First Fit";
    }
    
    @Override
    public Collection<Bin> solveOnline(ItemReader input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Bin> solveOffline(ItemReader input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    
}
