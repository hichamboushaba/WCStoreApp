//
//  SearchScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

class SearchViewModelWrapper: ViewModelWrapper<SearchViewModel> {
    @Published var searchQuery: String!
    var productsPagingObservable: PagingObservable<ProductUiModel>!

    init() {
        super.init()
        searchQuery = viewModel.searchQueryNativeValue
        productsPagingObservable = PagingObservable(flow: viewModel.products)

        assignToPublished(from:\.searchQueryNative, to: &$searchQuery)
    }
}

struct SearchScreen: View {
    @StateObject private var viewModelWrapper = SearchViewModelWrapper()
    
    @State private var isEditing = false
    
    private var viewModel: SearchViewModel {
        viewModelWrapper.viewModel
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            VStack {
                SearchBar(searchText: Binding(
                    get: { viewModelWrapper.searchQuery },
                    set: {
                        viewModel.onQueryChanged(query: $0)
                    }))
                ProductsList(
                        productsPagingObservable: viewModelWrapper.productsPagingObservable,
                        onProductClick: viewModel.onProductClicked
                )
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
