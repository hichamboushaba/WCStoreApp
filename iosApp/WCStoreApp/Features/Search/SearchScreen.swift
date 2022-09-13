//
//  SearchScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

struct SearchScreen: View {
    private let viewModel = KoinKt.get(objCClass: SearchViewModel.self) as! SearchViewModel
    @FlowWrapper private var products: ProductsUiListState
    @FlowWrapper private var searchQuery: String
    
    @State private var isEditing = false
    
    init() {
        _products = FlowWrapper(
            viewModel.productsNative,
            initialValue: ProductsUiListState.init(products: [], hasNext: true, state: LoadingState.loading)
        )
        _searchQuery = FlowWrapper(viewModel.searchQueryNative, initialValue: viewModel.searchQueryNativeValue)
    }
    
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            VStack {
                SearchBar(searchText: Binding(
                    get: { searchQuery },
                    set: {
                        viewModel.onQueryChanged(query: $0)
                    }))
                ProductsList(productsState: products, onProductClick: viewModel.onProductClicked, loadNext: viewModel.loadNext)
            }
        }
    }
}

private struct SearchBar: View {
    
    @Binding var searchText: String
    
    var body: some View {
        ZStack {
            HStack {
                Image(systemName: "magnifyingglass")
                TextField("Search ..", text: $searchText)
                
                if !searchText.isEmpty {
                    Button(action: {
                        searchText = ""
                    }) {
                        Text("Cancel")
                    }
                    .padding(.trailing, 10)
                    .transition(.move(edge: .trailing))
                }
            }
            .padding(.leading, 13)
        }
        .frame(height: 40)
        .background(Color(.lightGray))
        .cornerRadius(8)
        .padding()
    }
}
