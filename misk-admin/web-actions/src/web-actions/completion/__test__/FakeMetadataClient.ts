import MetadataClient from '@web-actions/api/MetadataClient';
import { MiskActions } from '@web-actions/api/responseTypes';

export const MyAction = {
  name: 'MyAction',
  requestType: 'MyActionRequest',
  pathPattern: '/api/v1/my-action',
  requestMediaTypes: ['application/x-protobuf'],
  types: {
    MyActionRequest: {
      fields: [
        {
          name: 'text',
          type: 'String',
          repeated: false,
          annotations: [],
        },
        {
          name: 'enum',
          type: 'Enum<Type,A,B,C>',
          repeated: false,
          annotations: [],
        },
        {
          name: 'object',
          type: 'my-object-type',
          repeated: false,
          annotations: [],
        },
        {
          name: 's-array',
          type: 'String',
          repeated: true,
          annotations: [],
        },
      ],
      'my-object-type': {
        fields: [],
      },
    },
  },
};

export default class FakeMetadataClient implements MetadataClient {
  async fetchMetadata(): Promise<MiskActions> {
    return Promise.resolve({
      MyAction: {
        name: 'MyAction',
        defaultCallable: MyAction,
        all: [MyAction],
      },
    });
  }
}
