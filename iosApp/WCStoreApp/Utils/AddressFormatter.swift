//
//  AddressFormatter.swift
//  WCStoreApp
//
//  Created by Hicham on 16/9/2022.
//

import Foundation
import WCStoreAppKmm

extension Address {
    func format() -> String {
        return """
        \(firstName) \(lastName)
        \(phone ?? "")
        
        \(street1)
        \(street2 ?? "")
        \(city), \(state) \(postCode)
        \(country)
        """
    }
}
